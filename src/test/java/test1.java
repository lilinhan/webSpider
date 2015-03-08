import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by lewin on 11/25/14.
 */
public class test1 {
    public static void main(String[] args)throws IOException{
        //定义URL的集合

        //获取天堂BBS的HTML文档
        Document document = Jsoup.connect("http://www.tiantangbbs.com/").userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36").get();
        //获取天堂BBS上面的电影URL
        Elements elements = document.getElementsByTag("span");
        for (Element element : elements)  {
            Elements eles = element.select("a[href]");
            System.out.println(eles.attr("abs:href"));
        }
    }
}
