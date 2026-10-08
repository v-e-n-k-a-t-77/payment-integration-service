package com.example.payment_service.client;

import com.example.payment_service.exception.PartnerApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class PartnerApiClient {

   private static final Logger log = LoggerFactory.getLogger(PartnerApiClient.class);
   private static final int MAX_ATTEMPTS=3;

   private final HttpClient httpClient;
   private final String statusUrlTemplate;
   private final Duration requestTimeout;

   public PartnerApiClient(@Value("${partner.api.status-url}") String statusUrlTemplate,
                           @Value("${partner.api.timeout-seconds}") long timeoutSeconds){
       this.statusUrlTemplate = statusUrlTemplate;
       this.requestTimeout = Duration.ofSeconds(timeoutSeconds);
       this.httpClient = HttpClient.newBuilder()
               .connectTimeout(requestTimeout)
               .build();
   }

   public String fetchStatus(Long paymentId){

       String url=statusUrlTemplate.replace("{id}",String.valueOf(paymentId));

       HttpRequest request=HttpRequest.newBuilder()
               .uri(URI.create(url))
               .timeout(requestTimeout)
               .header("Accept","application/json")
               .GET()
               .build();

       for(int attempt=1;attempt<=MAX_ATTEMPTS;attempt++){
           try{
               HttpResponse<String> response=httpClient.send(request,HttpResponse.BodyHandlers.ofString());
               int code=response.statusCode();
               log.info("Partner API attempt {} returned status {}", attempt, code);

               if (code >= 200 && code < 300) {
                   return "SUCCESS";
               }
               if (code >= 400 && code < 500) {
                   return "FAILED";
               }
               log.warn("Partner API server error {}, will retry if attempts remain", code);

           } catch (IOException e) {
               log.warn("Partner API attempt {} failed: {}", attempt, e.getMessage());
           } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               throw new PartnerApiException("Call to partner API was interrupted");
           }
           if(attempt<MAX_ATTEMPTS){
               pause(500L*attempt);
           }
       }
       throw new PartnerApiException(
               "Partner API unavailable after " + MAX_ATTEMPTS + " attempts");

}
       private void pause(long millis){
           try{
               Thread.sleep(millis);
           }catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               throw new PartnerApiException("Retry wait was interrupted");
       }
   }
}
