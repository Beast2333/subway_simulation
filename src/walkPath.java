import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class walkPath {
    int[][][] path;

    public ArrayList<String> get_csv_list(String dir_path){
        File dir = new File(dir_path);
        File[] file_list = dir.listFiles();
        ArrayList<String> csv_path_list = new ArrayList<>();

        for(int i = 0; i<file_list.length;i++){
            File f = file_list[i];
//            String f_name = f.getName().substring(f.getName().length()-3);
//            System.out.println(f_name);
            if(f.isFile() && "csv".equals(f.getName().substring(f.getName().length()-3))){
                csv_path_list.add(f.getAbsolutePath());
            }
        }
        return csv_path_list;
    }

    public int[][] get_csv_data(String csv_dir, int len){
        int[][] path_data = new int[len][len];
        String[] path_string;

        try{
            BufferedReader br = new BufferedReader(new FileReader(new File(csv_dir)));
            String line = null;
            int index = 0;
            while ((line = br.readLine()) != null) {
                path_string = line.split(",");
                for(int i=0;i<path_string.length;i++){
                    path_data[index][i] = Integer.parseInt(path_string[i]);
                }
                index += 1;
            }
        }catch(IOException e){
            System.out.println("文件读写出错");
        }
        return path_data;
    }

    public int get_len(String csv_dir){
        int len = 0;
        String[] path_string;
        try{
            BufferedReader br = new BufferedReader(new FileReader(new File(csv_dir)));
            String line = null;
            line = br.readLine();
            path_string = line.split(",");
            len = path_string.length;

        }catch(IOException e){
            System.out.println("文件读写出错");
        }
        return len;
    }

    public int[][][] get_all_csv(String dir){
        ArrayList<String> csv_path_list = get_csv_list(dir);
        int len = get_len(csv_path_list.get(0));
        int[][][] path_data = new int[csv_path_list.size()][len][len];
//        int[][] path_data0 = new int[len][len];
        path_data = new int[csv_path_list.size()][len][len];
        for(int i=0;i<csv_path_list.size();i++){
            path_data[i] = get_csv_data(csv_path_list.get(i), len);
            }
        path = path_data;
        return path_data;
    }
}
