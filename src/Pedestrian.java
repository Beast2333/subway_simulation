import java.util.Arrays;

public class Pedestrian {
    // 对象相关
    public int pedestrian_id;
    public long time_generate;
    public long time_destroy;
    // OD
    public Node origin;
    public Node destination;
    // 行走相关
    public Node location;
    public Node next_step;
    public double distanceToLeaveCurrentNode; // 距离
    public int[] forbidden_list; // 禁忌表
    public int[] pedestrian_path; // 路径表

    public static int[] list_append(int a, int[] b){
        int length = b.length;
        int[] temp = new int[length+1];
        for(int i=0;i<length;i++){
            temp[i] = b[i];
        }
        temp[length]  = a;
        return temp;
    }

    public static int[][] list_append(int[] a, int[][] b){
        int length0 = b.length;
        int length1 = b[0].length;
        int[][] temp = new int[length0+1][length1];
        for(int i=0;i<length0;i++){
            temp[i] = b[i];
        }
        temp[length0]  = a;
        return temp;
    }

    public double[] possibility_choice(int begin, int[][][] path, double alpha, double beta){
        double denominator = 0; // 分母
        int length = path[0][begin].length;
        double[] possibility_list = new double[length];
        for(int i=0;i<length;i++){ // 分母计算
            if(path[0][begin][i] != 0){
//                denominator += path[0][begin][i]**alpha + path[1][begin][i]**beta;
                double a = path[0][begin][i];
                double b = path[1][begin][i];
                denominator += Math.pow(a, alpha) + Math.pow(b, beta);
            }
        }
        for(int k=0;k<length;k++) {
            if(path[0][begin][k] != 0 && Arrays.asList(this.forbidden_list).contains(k)){
                this.forbidden_list = list_append(k, this.forbidden_list);
                // print(self.forbidden_list)
                double heuristic = 1 / path[0][begin][k];
                int ph = path[1][begin][k];
                double possibility = (Math.pow(heuristic, alpha) + Math.pow(ph, beta)) / denominator;
                possibility_list[k] = possibility;
            }
            else{possibility_list[k] = 0;}
        }
        return possibility_list;
    }
}
