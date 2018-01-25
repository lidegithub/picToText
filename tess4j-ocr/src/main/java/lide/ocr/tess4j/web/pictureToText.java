package lide.ocr.tess4j.web;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

@RestController
@RequestMapping("/change")
public class pictureToText {
	
	/**   
     * 文件上传具体实现方法;   
     *    
     * @param file   
     * @return   
     */    
    @RequestMapping("/upload")    
    @ResponseBody    
    public boolean handleFileUpload(@RequestParam("file") MultipartFile file) {    
        if (!file.isEmpty()) {    
            try {    
                /*   
                 * 这段代码执行完毕之后，图片上传到了工程的跟路径； 大家自己扩散下思维，如果我们想把图片上传到   
                 * d:/files大家是否能实现呢？ 等等;   
                 * 这里只是简单一个例子,请自行参考，融入到实际中可能需要大家自己做一些思考，比如： 1、文件路径； 2、文件名；   
                 * 3、文件格式; 4、文件大小的限制;   
                 */    
                BufferedOutputStream out = new BufferedOutputStream(    
                        new FileOutputStream(new File(    
                                file.getOriginalFilename())));    
                System.out.println(file.getName());  
                out.write(file.getBytes());    
                out.flush();    
                out.close();    
            } catch (FileNotFoundException e) {    
                e.printStackTrace();    
//                return "上传失败," + e.getMessage(); 
                return false;
            } catch (IOException e) {    
                e.printStackTrace();    
//                return "上传失败," + e.getMessage();    
                return false;
            }    
    
//            return "上传成功";    
            return true;
    
        } else {    
//            return "上传失败，因为文件是空的.";
        	return false;
        }    
    }
	
	@RequestMapping("toText")
	public String toText(@RequestParam("file") MultipartFile file) throws IOException {
		
		System.out.println("toText enter");
		
		BufferedOutputStream out = new BufferedOutputStream(    
                new FileOutputStream(new File(    
                        file.getOriginalFilename())));    
        System.out.println(file.getName());  
        out.write(file.getBytes());    
        out.flush();    
        out.close();
		
		/*
		 * 
		 * // 这里对图片黑白处理,增强识别率.这里先通过截图,截取图片中需要识别的部分  
BufferedImage textImage = ImageHelper.convertImageToGrayscale(ImageHelper.getSubImage(panel.image, startX, startY, endX, endY));  
// 图片锐化,自己使用中影响识别率的主要因素是针式打印机字迹不连贯,所以锐化反而降低识别率  
// textImage = ImageHelper.convertImageToBinary(textImage);  
// 图片放大5倍,增强识别率(很多图片本身无法识别,放大5倍时就可以轻易识,但是考滤到客户电脑配置低,针式打印机打印不连贯的问题,这里就放大5倍)  
textImage = ImageHelper.getScaledInstance(textImage, endX * 5, endY * 5); 
		 * */
		String result = null;
//		File imageFile = new File("C:\\IMG_1570.JPG"); 
//		File imageFile = new File("/home/tesseract-ocr3.0.4/IMG_1570.JPG");

		File imageFile = null;
		try {
			imageFile=File.createTempFile("tmp", null);
		    file.transferTo(imageFile);
		    imageFile.deleteOnExit();        
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		//读取图片对象  
        BufferedImage textImage = ImageIO.read(imageFile);    
       //获得图片的宽  
        int endX= textImage.getWidth();  
       //获得图片的高  
       int endY=textImage.getHeight();  
    // 这里对图片黑白处理,增强识别率.这里先通过截图,截取图片中需要识别的部分  
       textImage = ImageHelper.convertImageToGrayscale(ImageHelper.getSubImage(textImage, 0, 0, endX, endY));  
       // 图片锐化,自己使用中影响识别率的主要因素是针式打印机字迹不连贯,所以锐化反而降低识别率  
       // textImage = ImageHelper.convertImageToBinary(textImage);  
       // 图片放大5倍,增强识别率(很多图片本身无法识别,放大5倍时就可以轻易识,但是考滤到客户电脑配置低,针式打印机打印不连贯的问题,这里就放大5倍)  
//       textImage = ImageHelper.getScaledInstance(textImage, endX * 5, endY * 5); 
       
        ITesseract instance = new Tesseract();  // JNA Interface Mapping      
//         ITesseract instance = new Tesseract1(); // JNA Direct Mapping      
        instance.setLanguage("chi_sim");//添加中文字库    
        try {      
            result = instance.doOCR(textImage);      
            System.out.println(result);      
        } catch (TesseractException e) {      
            System.err.println(e.getMessage());      
        }
        
        return result;
	}
}
