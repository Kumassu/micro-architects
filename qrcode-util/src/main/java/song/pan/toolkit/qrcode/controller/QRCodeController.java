package song.pan.toolkit.qrcode.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import song.pan.toolkit.qrcode.util.QRCodeUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Base64;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Controller
@RequestMapping("/api")
public class QRCodeController {


    /**
     * 加了 @ResponseBody 注解之后，SpringMVC 不再去找视图，而是通过 HttpMessageConverter 这个接口的实现类而进行消息转化，
     * 当你的方法返回了 byte[] 的时候，Spring 则判断使用 ByteArrayHttpMessageConverter 来做消息转化，返回到前台，
     * 同样，我们返回 Json 和 xml 的时候，也是使用的（如果没有自定义配置）spring 自带的 MappingJackson2HttpMessageConverter 来做消息转化。
     * 同理，Spring 还提供了 BufferedImageHttpMessageConverter 这个实现，所以我们才能够通过返回 BufferedImage 来直接返回图片。
     */
    @RequestMapping(value = "/v1/qrcodes", method = RequestMethod.GET)
    public void images(String content, HttpServletResponse response) throws Exception {
        BufferedImage image = QRCodeUtils.toImage(content);
        response.setContentType("image/jpeg");
        ImageIO.write(image, "jpg", response.getOutputStream());
    }

}
