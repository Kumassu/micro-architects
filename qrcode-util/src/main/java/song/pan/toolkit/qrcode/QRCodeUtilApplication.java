package song.pan.toolkit.qrcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@SpringBootApplication
public class QRCodeUtilApplication {

    public static void main(String[] args) {
        SpringApplication.run(QRCodeUtilApplication.class);
    }

    @Bean
    public BufferedImageHttpMessageConverter bufferedImageHttpMessageConverter(){
        return new BufferedImageHttpMessageConverter();
    }

}
