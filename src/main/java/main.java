import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * Created by lewin on 3/8/15.
 * 抓取电影天堂（www.tiantangbbs.com）的最新的电影
 */
public class main {
    public static void main(String[] args) throws IOException {
        //获取天堂BBS的HTML文档
        Document document = Jsoup.connect("http://www.tiantangbbs.com/")
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
                .get();
        //获取天堂BBS上面的电影URL
        Elements elements = document.select("a");
        List urlList = new ArrayList();
        for (Element element : elements) {
            //抓取到URL
            String movieURL = element.attr("abs:href");
            //提取到有用的的URL
            if (movieURL.contains("thread") && movieURL.contains("html") && !movieURL.contains("83494")) {
                //将抓取到的URL添加到List中
                urlList.add(movieURL);
            }
        }
        //将List中重复的URL去除
        urlList = removeDuplicate(urlList);
//      //  计数URL个数测试去重是否正确
//        int i = 0;
//        for ( i = 0 ; i < urlList.size() ; i++ )  {
//            System.out.println(urlList.get(i));
//        }
//        System.out.println(i);

        //进入list中的每一个URL进行抓取
        FetchDataIntoDifferentURL fetchDataIntoDifferentURL = new FetchDataIntoDifferentURL();
        fetchDataIntoDifferentURL.FetchDataIntoDifferentURL(urlList);
    }

//将List中重复的URL去除
    public static List removeDuplicate(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object object = iterator.next();
            if (set.add(object)) {
                newList.add(object);
            }
        }
        return newList;
    }
}