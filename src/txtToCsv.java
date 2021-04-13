import java.io.*;
import java.util.*;
import java.lang.String;

public class txtToCsv {
    public void main(String filename){
        File writeFile = new File("./nodeRelation0312.csv");
        try{
            String line=null;
            String[] fieldData;
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "gb2312"));
//            Scanner sinScanner=new Scanner(new FileReader(filename));


            BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));

            while(( line = br.readLine() ) != null) {
//                line = sinScanner.nextLine();
                fieldData = line.split("\t");
                writeText.newLine();
                writeText.write(fieldData[0]+','+fieldData[1]+','+fieldData[2]+','+fieldData[3]+','+fieldData[4]+',');
                System.out.println(fieldData[2]);
            }
            writeText.flush();
            writeText.close();
        }catch (FileNotFoundException e){
            System.out.println("没有找到指定文件");
        } catch (IOException e) {
            System.out.println("文件读写出错");
        }
    }
}
