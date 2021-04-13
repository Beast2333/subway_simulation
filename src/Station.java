import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.ParseException;

public class Station {

    public int year=2018-1900;
    public int month=6;
    public int day=2;
    public long time_ref=0;
    public long time_start=7*60*60*1000;//起始时间
    public long time_end=10*60*60*1000;//结束时间
    //public int control_interval=300/5;
    public long[] time_start_control={(7*60+60)*60*1000,(7*60+60)*60*1000,(7*60+60)*60*1000,(7*60+60)*60*1000};
    public long[] time_end_control={(7*60+180)*60*1000,(7*60+180)*60*1000,(7*60+180)*60*1000,(7*60+180)*60*1000};//限流结束时间//public long time_start_control2=0;//绕行限流起始时间
    // public long time_end_control[]={(7*60+77)*60*1000,(7*60+40)*60*1000,(7*60+73)*60*1000};//限流结束时间//public long time_start_control2=0;//绕行限流起始时间
    //public long time_end_control2=0;//绕行限流结束时间
    public int[] nodes_control_in={158,1122,1113,110};//流入控制 G口  10号线西站厅  1号线站厅  BC口
    public int[][] nodes_control_rate={{29,30}};//绕行控制,对其中的节点流出客流做比例限制
    public int [] nodes_control_lenth={146};//绕行,设置26和146的比例互换
    public double [][] control_in_number=new double[4][6];
    /*	{{3051.501,	1700.737,	1822.574,	812.4366,	3388.438,	1988.814},

            {3185.991,	2420.27,	942.5112,	1233.757,	1473.526,	1017.067},
            {2149.609,	3305.929,	2741.586,	190.51,	2643.901,	1322.044},
            {2149.609,	3305.929,	2741.586,	190.51,	2643.901,	1322.044}};//new double[1][6];//每半小时的控制
*/	//public int [] control_length={750};//增加换乘距离到*米,
    //public double [] control_second={0};//增加换乘距离到*米,等待秒数
    //public double [] control_tran_number={0,0,0,0,0,0};//绕行的通过能力
    public int bControl=0;//0，无控制，1进站控制，2进站+站厅控制，3进站和换乘换乘同时控制，
    public double[] stars_r={0.2,0.4,0.4};//
    public long time_interval=0;//=1000;	//单位节拍
    public long time_now;//当前仿真时刻
    public int time_now_index=0;//当前仿真时刻对应索引
    public int time_number_index=0;//仿真的节拍总数
    public long time_enterSPan=5*60*1000;//进站客流统计时段
    public double Refrate=1;
    public double RefBoard=1;
    public ArrayList<Node> SetA=new ArrayList <Node>();//定义待流出节点集合SetA
    Node[] nodes=null;//节点集合
    Node[] nodesFlow=null;
    Node[] nodes_father=null;
//    Line[]lines=null;//流线集合
//    public Solutioninfo solution=new Solutioninfo();
    //int nodeNum=0;
    static Station simu=new Station();
    int sumNum=0;


    public static void main(String[] args) {
////        //TODO 自动生成的方法存根
//        long time0=System.currentTimeMillis();
//        simu.setParam();
//        simu.init();
//        long time=System.currentTimeMillis();
//        simu.run();
//        long time2=System.currentTimeMillis();
//        System.out.println("run time:"+(time2-time));
//        System.out.println("all run time:"+(time2-time0));
//        System.out.println("average waittime:"+simu.ComputewaitAverofStation());
////        System.out.println("extra number:"+simu.solution.ex_number);
//        System.out.println("average staytime:"+simu.ComputeStayAverofStation());
//        simu.writeNodeAll();
//        long time3=System.currentTimeMillis();
//        System.out.println("all time:"+(time3-time0));
        //simu.writeLineAll();

//        Node te = new Node();
//        Node.Path tes = te.new Path();
//        for(int i=0;i<10;i++){
//            Pedestrian p1 = new Pedestrian();
//            tes.addWaiting_in_pedestrian(p1);
//        }
//
//        System.out.println(tes.getWaiting_in_pedestrian());
//        tes.deleteWaiting_in_pedestrian();
//        System.out.println(tes.getWaiting_in_pedestrian());
//        Pedestrian p1 = new Pedestrian();
//        tes.addWaiting_in_pedestrian(p1);
//        System.out.println(tes.getWaiting_in_pedestrian());
//        txtToCsv a = new txtToCsv();
//        a.main("/Users/wangkang/Desktop/毕设/程序发王旭/Data/nodeRelation0312.txt");

//        int a = 4;
//        int[] b = {1, 2, 3};
//        b = Pedestrian.list_append(a, b);
//        System.out.println(Arrays.toString(b));

//        int[][][] a = new int[5][4][3];
//        System.out.println(a.length + ","+ a[0].length + "," + a[0][0].length);
//    }

//        walkPath a = new walkPath();
//        ArrayList<String> b = a.get_csv_list("./walkPathData");
//        System.out.println(b);
//        for(int i = 0;i<b.size();i++){
//            a.get_csv_data(b.get(i));
//        }
//        int[][] a = new int[5][6];
//        System.out.println(a.length);
        walkPath a = new walkPath();
        int[][][] b = a.get_all_csv("./walkPathData");
        System.out.println(Arrays.deepToString(a.path));


    }
    //////////////
    //
    public void init(){
        this.loadNode("Data//node1222.txt");//车站节点
//        this.loadRelationFC("Data//nodeRFC0312.txt");//父子关系，可忽略
//        this.loadRelation("Data//nodeRelation0312.txt");//流线关系，新建表格表示网络节点连接关系
        this.loadNodes("Data//nodes0312.txt");//节点优先序列,表格需要修改
        this.loadalighting("Data//alighting1.txt");//下车
        this.loadEntering("Data//enter.txt");//进站
    }
    /////////////
    //每次加载控制方案前重置
    public void reset(){
        //重置仿真时钟，当前时间和当前索引

        //重置所有节点的状态和统计结果
        for(int i=0;i<this.nodes.length;i++){
            nodes[i].reset();
        }
        for(int i=0;i<this.nodes_father.length;i++){
            nodes_father[i].reset();
        }
    }

    ///////////////////
    //设置运行参数
    public void setParam(){
        //运行的起止时间、节拍
        this.time_interval=1000;
        Date date=new Date();
        date.setYear(this.year);
        date.setMonth(this.month);
        date.setDate(this.day);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        this.time_ref=date.getTime();
        this.time_end+=this.time_ref;
        this.time_start+=this.time_ref;
//		this.time_end_control+=this.time_ref;
//		this.time_start_control+=this.time_ref;
        for(int i=0;i<this.nodes_control_in.length;i++){
            this.time_end_control[i]+=this.time_ref;
            this.time_start_control[i]+=this.time_ref;
        }

        double temp=(this.time_end-this.time_start)/this.time_interval;
        this.time_number_index=(int)temp;
        if((this.time_end-this.time_start)%this.time_interval!=0)
            time_number_index++;
    }

    /////////////////////
    //加载节点序列
    public void loadNodes(String filename){
        //	节点编号id	节点类别type	节点描述describe	长l	宽w	面积s	容量cap	流量限制flim	速度限制vlim
        try {
            String line;
            String[] fieldData;
            Scanner sinScanner=new Scanner(new FileReader(filename));
            //line=sinScanner.nextLine();
            int count=0;
            while(sinScanner.hasNext())
            {
                sinScanner.nextLine();
                count++;
            }
            this.nodesFlow=new Node[count];
            sinScanner=new Scanner(new FileReader(filename));
            //line=sinScanner.nextLine();
            count=0;
            while(sinScanner.hasNext())
            {
                line=sinScanner.nextLine();
                int id=Integer.parseInt(line);
                Node node=this.getNodeFromID(id, 0);
                nodesFlow[count]=node;
                count++;
            }

        } catch (IOException e) {
            // TODO: handle exception
        }
    }
    ///////////////////
    //加载基本信息；
    public void loadNode(String filename){
        //	节点编号id	节点类别type	节点描述describe	长l	宽w	面积s	容量cap	流量限制flim	速度限制vlim
        try {
            String line;
            String[] fieldData;
            Scanner sinScanner=new Scanner(new FileReader(filename));
            line=sinScanner.nextLine();
            int count1=0,count2=0;
		    /*sinScanner.nextLine();//跳过第一行
			int count=0;
			while(sinScanner.hasNext())
			{
				sinScanner.nextLine();
				count++;
			}
			this.nodeNum=count;
			this.nodes=new Node[count];
			sinScanner=new Scanner(new FileReader(filename));//重新读文件
			sinScanner.nextLine();//跳过第一行
			count=0;*/
            ArrayList<Node> nodestemp=new ArrayList<Node>();
            while(sinScanner.hasNext())
            {
                line=sinScanner.nextLine();
                fieldData=line.split("\t");
                Node node =new Node();
                node.station=this;
                node.init();
                node.id=Integer.parseInt(fieldData[0]);
                node.type=Integer.parseInt(fieldData[1]);
                node.describe=fieldData[2];
                node.length=Double.parseDouble(fieldData[3]);
                node.weight=Double.parseDouble(fieldData[4]);
                node.area=Double.parseDouble(fieldData[5]);
                node.capacity=Double.parseDouble(fieldData[6]);
                node.cap_remain=node.capacity;
                node.flow_limit=Double.parseDouble(fieldData[7]);
                node.v_limit=Double.parseDouble(fieldData[8]);
                node.type_dir=Integer.parseInt(fieldData[9]);//0普通节点，1子节点，2父节点
                node.cap_safe=Integer.parseInt(fieldData[10]);
                if(node.type_dir==2)
                    count2++;
                else
                    count1++;
                //初始化分时得速度与密度
                int len=time_number_index;
                for(int i=0;i<len;i++){

                    node.velocitys[i]=node.v_limit;

                    node.flow_in_rate[i]=node.flow_limit;
                }
                if(node.type==7)
                    this.SetA.add(node);
                // System.out.println(node.id+"\t"+node.type+"\t"+node.describe+"\t"+node.v_limit);
                nodestemp.add(node);
            }
            nodestemp.toArray();
            SetA.toArray();
            this.nodes=new Node[count1];
            this.nodes_father=new Node[count2];
            int j=0,k=0;
            for(int i=0;i<nodestemp.size();i++){
                Node node=nodestemp.get(i);
                if(node.type_dir==2){
                    this.nodes_father[k]=node;
                    k++;
                }
                else{
                    this.nodes[j]=node;
                    j++;
                }

            }
        } catch (IOException e) {
            // TODO: handle exception
        }

    }
//    public void loadRelationFC(String filename){//加载父子关系表
//        //父节点编号，子节点编号
//        try {
//            String strline;
//            String[] fieldData;
//            Scanner sinScanner=new Scanner(new FileReader(filename));
//            sinScanner.nextLine();//跳过第一行
//            while(sinScanner.hasNext())
//            {
//                strline=sinScanner.nextLine();
//                fieldData=strline.split("\t");
//                String temp=fieldData[1];
//                if(fieldData[1].startsWith("\""))
//                    temp=fieldData[1].substring(1,fieldData[1].length()-1);
//                if(temp.startsWith("“"))
//                    temp=temp.substring(1,fieldData[1].length()-3);
//                String[] nodes=temp.split(",");
//
//                int count=nodes.length;
//                int IDF=Integer.parseInt(fieldData[0]);
//                Node nodeF=this.getNodeFromID(IDF, 2);//得到父节点
//                nodeF.children=new Node[count];//初始化父节点包含的子节点数据
//                for(int i=0;i<count;i++){
//                    int IDC=Integer.parseInt(nodes[i]);//得到子节点ID
//                    Node nodeC=this.getNodeFromID(IDC, 1);
//                    nodeC.father=nodeF;//增加每个子节点的父节点
//                    nodeF.children[i]=nodeC;//增加父节点的子节点
//                }
//
//
//            }
//        } catch (IOException e) {
//            // TODO: handle exception
//        }
//    }
//    ////////////////////
//    //加载节点间关系；
//    public void loadRelation(String filename){
//        //	节点编号	对应流出节点序列
//        //	1	3,12,13,14,60
//        try {
//            String line;
//            String[] fieldData;
//            Scanner sinScanner=new Scanner(new FileReader(filename));
//            sinScanner.nextLine();//跳过第一行
//            while(sinScanner.hasNext())
//            {
//                line=sinScanner.nextLine();
//                fieldData=line.split("\t");
//                String temp=fieldData[3];
//                if(fieldData[3].startsWith("\""))
//                    temp=fieldData[3].substring(1,fieldData[3].length()-1);
//                String[] Rels=temp.split(",");
//                int ID=Integer.parseInt(fieldData[0]);
//                Node node =this.getNodeFromID(ID, 0);
//                node.relOut=new Node[Rels.length];
//                node.beOut=new int[Rels.length];
//                for(int i=0;i<Rels.length;i++){
//                    node.relOut[i]=this.getNodeFromID(Integer.parseInt(Rels[i]), 0);
//                    node.beOut[i]=0;
//                }
//                //System.out.println(node.id+"\t"+node.relOut.length+"\t");
//                temp=fieldData[4];
//                if(fieldData[4].startsWith("\""))
//                    temp=fieldData[4].substring(1,fieldData[4].length()-1);
//                String[] Rates=temp.split(",");
//                node.rateOut=new double[Rates.length];
//                for(int i=0;i<Rates.length;i++){
//                    node.rateOut[i]=Double.parseDouble(Rates[i]);
//                }
//                //System.out.println(node.id+"\t"+node.relOut.length+"\t"+Rates.length);
//            }
//        } catch (IOException e) {
//            // TODO: handle exception
//        }
//        //计算并增加节点的流入节点
//        for(int i = 0; i < this.nodes.length; i++){
//            if(this.nodes[i].relOut==null)
//                continue;
//            int len=this.nodes[i].relOut.length;
//            for(int j=0;j<len;j++){
//                Node nodeOut=nodes[i].relOut[j];
//                nodeOut.relInTemp.add(nodes[i]);
//            }
//        }
//        System.out.println();
//        for(int i = 0; i < this.nodes.length; i++){
//            Node node=this.nodes[i];
//            node.relIn=new Node[node.relInTemp.size()];
//            System.out.print(node.id+"\t");
//            for(int j=0;j<node.relIn.length;j++){
//                node.relIn[j]=node.relInTemp.get(j);
//                System.out.print(node.relIn[j].id+"\t");
//            }
//            System.out.println();
//            node.relInTemp=null;
//        }
//    }

    /////////////////////
    //根据ID得到对应的节点
    public Node getNodeFromID(int ID, int type){
        Node[] nodes=null;
        if(type==2)
            nodes=this.nodes_father;
        else
            nodes=this.nodes;
        for(int i=0;i<nodes.length;i++){
            Node node=nodes[i];
            if(node.id==ID)
                return node;
        }
        return null;
    }
    //////////////////////
    //根据时间得到对应的索引编号
    public int getindexofTime(String timeStr){
        String[] times=timeStr.split(":");
        long time=(long)Integer.parseInt(times[0])*3600+(long)Integer.parseInt(times[1])*60;
        if(times.length==3)
            time+=Integer.parseInt(times[2]);
        time*=1000;
        time+=this.time_ref;
        int index=0;
        index=getindexofTime(time);
        return index;
    }
    public int getindexofTime(long time){
        long timespan=time-this.time_start;
        int interval=(int)(timespan/this.time_interval);
        return interval;
    }
    public long getTimeofIndex(int index){
        long time=this.time_start+index*this.time_interval;

        return time;
    }
    ///////////////
    //加载列车到达下车客流，节点编号+客流的分时供给，供给函数类别为阶跃函数
    public void loadalighting(String filename){//加载列车到站、停靠时间，以及列车到站时的车上人数、下车人数
        //列车节点 时刻 人数将下车人数按照仿真节拍加载到列车节点的分时供给量中
        //时间	节点编号	下车人数
        //0:00:00
        try {
            String strline;
            String[] fieldData;
            Scanner sinScanner=new Scanner(new FileReader(filename));
            sinScanner.nextLine();//跳过第一行
            while(sinScanner.hasNext())
            {
                strline=sinScanner.nextLine();
                fieldData=strline.split("\t");
                String timeStr=fieldData[0];
                int ID=Integer.parseInt(fieldData[1]);
                Node node=this.getNodeFromID(ID,0);
                double number=Double.parseDouble(fieldData[2].trim());
                this.sumNum+=number;
                int timeIndex=getindexofTime(timeStr);
                if(timeIndex>=this.time_number_index)
                    continue;
                // node.supplys_out_acc[timeIndex]=number;//下车人数
                //node.supplys_out_new[timeIndex]=node.supplys_out_acc[timeIndex];
                node.status[timeIndex]=3;//列车到达
                int alightingTime=1;//下车时间
                alightingTime+=(number/(node.flow_in_rate[timeIndex]*node.weight));//下车人数/下车速度
                //停站时间内都设为status=1
                int alightend=0;
                for(int i=1;i<30000/this.time_interval;i++){
                    if(timeIndex+i>=this.time_number_index)
                        break;
                    if(alightend==0) {
                        node.status[timeIndex+i]=2;//列车到达乘客下车时间
                        double temp=node.flow_in_rate[timeIndex+i]*node.weight;
                        if(number-temp>=0) {
                            node.supplys_out_acc[timeIndex+i]=temp;//下车人数
                            number-=temp;
                        }

                        else {
                            node.supplys_out_acc[timeIndex+i]=number;//下车人数
                            alightend=1;
                        }
                        node.supplys_out_new[timeIndex+i]=node.supplys_out_acc[timeIndex+i];
                    }
                    else
                        node.status[timeIndex+i]=1;//列车乘客上车时间
                }
                node.ontrain[timeIndex]=Double.parseDouble(fieldData[3]);//车上原有人数
                // node.boardtrain[timeIndex]=Double.parseDouble(fieldData[4])*RefBoard;
                //System.out.println( ID+"\t"+number+"\t"+timeIndex+"\t"+timeIndex);

            }
        } catch (IOException e) {
            // TODO: handle exception
        }
    }
    ///////////////
    //加载进站客流，增加进站节点，节点编号+客流的分时到达供给，供给函数类别为泊松函数
    public void loadEntering(String filename){
        //将进站人数按照仿真节拍加载到列车节点的分时供给量中
        //	时间	进站节点编号	进站人数
        //0:00:00
        //0:00:00
        try {
            String strline;
            String[] fieldData;
            Scanner sinScanner=new Scanner(new FileReader(filename));
            sinScanner.nextLine();//跳过第一行
            while(sinScanner.hasNext())
            {
                strline=sinScanner.nextLine();
                fieldData=strline.split("\t");
                String timeStr=fieldData[0].split("-")[0];
                int ID=Integer.parseInt(fieldData[1]);
                Node node=this.getNodeFromID(ID,0);
                double number=Double.parseDouble(fieldData[2]);
                this.sumNum+=number;
                ///////////////////////
                //根据节拍将流量分配到时段内的各个节拍中
                //计算节拍个数，
                int count=(int)(this.time_enterSPan/this.time_interval);
                //计算开始节拍
                int timeIndex=getindexofTime(timeStr);
                for(int i=0;i<count;i++){
                    if(timeIndex+i>=this.time_number_index)
                        break;
                    node.supplys_out_acc[timeIndex+i]=number/count*Refrate;
                    node.supplys_out_new[timeIndex+i]=node.supplys_out_acc[timeIndex+i];
                }
                // System.out.println( ID+"\t"+number+"\t"+timeIndex+"\t"+count);

            }
        } catch (IOException e) {
            // TODO: handle exception
        }
    }


    //////////////////
    //仿真运行
    public void run(){
        this.time_now=this.time_start;
        this.time_now_index=0;
        //状态初始化；
        while(this.time_now<this.time_end){
            if(this.bControl==3&&(this.time_now==this.time_start_control[1]
                    ||this.time_now==this.time_end_control[1])){//控制开始设置控制或控制结束
                for(int i=0;i<this.nodes_control_rate.length;i++){
                    for(int j=0;j<nodes_control_rate[i].length;j++){
                        Node node=this.getNodeFromID(nodes_control_rate[i][j], 0);
                        //for(int j=0;j<node.relOut.length;j++){
                        double temp=node.rateOut[0];
                        node.rateOut[0]=node.rateOut[1];//比例互换,如果多于两个则要修改为循环
                        node.rateOut[1]=temp;
                    }

                    //}
                }
            }
            for(int i=0;i<this.nodes.length;i++)//循环各节点,初始化和更新列车到站客流
            {
                Node node=this.nodes[i];
                if(node.rateOut==null)
                    continue;
                for(int j=0;j<node.rateOut.length;j++){
                    node.beOut[j]=0;
                    node.outCount=0;
                }
                this.updateTrainNode(node);//根据每个节点的供给更新被供给
            }
            for(int i=0;i<this.nodes.length;i++)//循环各节点,更新节点的被供给量
            {
                Node node=this.nodes[i];
                this.updateSupplyofNode(node);//根据每个节点的供给更新被供给
            }
            //////////////
//            //遍历父节点集合，更新子节点分配的剩余容量
//            for(int i=0;i<this.nodes_father.length;i++)//循环各节点
//            {
//                Node node=this.nodes_father[i];
//                this.updateRemainofChNode(node);//更新流出和流入
//            }
			/*for(int i=0;i<this.nodes.length;i++)//循环各节点
			{
				Node node=this.nodes[i];
				this.updateFlowofNode(node);//更新流出和流入
			}*/
            this.updateFlowofNodes();
            for(int i=0;i<this.nodes.length;i++)//循环各节点
            {
                Node node=this.nodes[i];
                this.updateValueofNode1(node);//更新节点其他状态值
            }
            for(int i=0;i<this.nodes_father.length;i++)//循环各节点
            {
                Node node=this.nodes_father[i];
                this.updateValueofNodeF(node);//更新节点其他状态值
            }
            for(int i=0;i<this.nodes.length;i++)//循环各节点
            {
                Node node=this.nodes[i];
                this.updateValueofNode2(node);//更新节点其他状态值
            }
            this.time_now+=this.time_interval;
            this.time_now_index++;
        }
        ComputewaitAverofStation();
        ComputeStayAverofStation();
    }
    /////////////////
    //更新车站的节点客流流动
    public void updateFlowofNodes(){
        for(int i=0;i<this.nodesFlow.length;i++){
            Node node=this.nodesFlow[i];
            if(node==null)
                System.out.println(i);
            this.updateFlowofNode(node);
            //	if(node.type!=5)
            {			}

        }

    }
    /*public void updateFlowofNodes(){

         * 定义待流出节点集合SetB
         * 将出站节点在loadnode时加入集合SetA，将SetA元素入SetB
         * 如SetB集合不为空
         * 	从集合B中取出头节点node，
         * 如果是出站节点，则更新流出beflow=1;
         * 否则
         * 	遍历节点的流出节点nodeout，
         *     如果节点nodeout已流出,则更新nodeout的流入与node在这个方向的流出，以及流出次数
         *   如果node的流出次数（成员变量,每个节拍需要初始化为0）达到流出节点数量，
         *       则beflow=1,else beflow=0;，
         * if beflow=1将node的流入节点集合中的节点分别加入SetB
         *   否则，将node加入setB
         *
        ArrayList<Node> SetB=new ArrayList <Node>();//定义待流出节点集合SetA
        for(int i=0;i<this.SetA.size();i++){
            //将出站节点加入集合SetB
            SetB.add(SetA.get(i));
        }

        while(!SetB.isEmpty()){

            int beflow=0;
            //如SetB集合不为空
            Node node=SetB.get(0);//从集合B中取出头节点node
        //	System.out.println("this.time_now_index"+this.time_now_index+"node.id"+node.id);
        //	if(node.id==73&&this.time_now_index==26)
            //	System.out.println("node.id==73&&this.time_now_index==26"+node.type);
            if(node.type==7||node.type==5){
                //如果是出站节点，则更新流出beflow=1;
                updateFlowofNode(node);
                beflow=1;
                node.outCount++;
                node.beOut[0]++;
            }
            else{
                for(int j=0;j<node.relOut.length;j++){
                    if(node.beOut[j]==1)
                        continue;
                    Node nodeOut=node.relOut[j];//遍历节点的流出节点nodeout
                    if(nodeOut.outCount==nodeOut.relOut.length){
                        //如果节点nodeout已流出,
                        updateFlowofNode(node,j);//则更新nodeout的流入与node在这个方向的流出，以及流出次数
                        node.outCount++;
                        node.beOut[j]++;
                    }

                }

            }
            //更新node的存量;
            SetB.remove(0);
            if(node.outCount==node.relOut.length){
                // 如果node的流出次数（成员变量,每个节拍需要初始化为0）达到流出节点数量，
                beflow=1; // 则beflow=1,else beflow=0;，
                //node.num_current=Math.max(node.num_current, node.supplys_out_acc[this.time_now_index]);
                //node.num_current+=node.flow_ins[this.time_now_index];
                node.num_current-=node.flow_outs[this.time_now_index];//更新存量
                node.cap_remain=Math.max(0, node.capacity-node.num_current);//更新剩余容量

                if(node.relIn!=null){
                    for(int j=0;j<node.relIn.length;j++){
                        SetB.add(node.relIn[j]);//将node的流入节点集合中的节点分别加入SetB
                    }
                }

            }
            else{
                SetB.add(node);//将node加入setB
            }


        }



    }*/
    ///////////////
    //更新列车状态
    public void updateTrainNode(Node node){
        if(node.type==5)//节点类别为列车
        {
            if(node.status[this.time_now_index]==3)//有列车到达
            {
                node.num_current=node.ontrain[this.time_now_index];
                node.cap_remain=node.capacity-node.num_current;
                //node.cap_remain=node.boardtrain[this.time_now_index];
            }
        }
    }
    ///////////////////
    //节点被供给量计算
    public void updateSupplyofNode(Node node){//根据供给量以及流向比例，更新被供给
        if(node.rateOut==null)
            return;
        double temp=0;
        for(int i=0;i<node.relOut.length;i++){
            //遍历流出关系节点
            Node nodeTo=node.relOut[i];
            double flow=0;
            if(i==node.relOut.length-1)
                flow=node.supplys_out_acc[time_now_index]-temp;
            else
                flow=node.supplys_out_acc[time_now_index]*node.rateOut[i];
            temp+=flow;
            nodeTo.supplys_in_acc[this.time_now_index]+=flow;//根据比例对每一个流入计算供给量供给总量=(各个流入节点*流入比例)之和
        }

    }
    public void updateRemainofChNode(Node node){//根据供给更新子节点分配到的剩余空间
        for(int i=0;i<this.nodes_father.length;i++){
            //遍历父节点
            Node nodeF=this.nodes_father[i];
            //记录父节点的剩余空间
            //nodeF.supplys_in_acc[this.time_now_index]=0;
            double sum=0;
            for(int j=0;j<nodeF.children.length;j++){
                Node nodeC=nodeF.children[j];
                sum+=nodeC.supplys_in_acc[this.time_now_index];//计算每个父节点的被供给总量
            }
            double temp=0;
            for(int j=0;j<nodeF.children.length;j++){
                Node nodeC=nodeF.children[j];
                double rate=1;
                if(sum!=0)
                    rate=nodeC.supplys_in_acc[this.time_now_index]/sum;//各个子节点的分配比例
                if(j<nodeF.children.length-1) {
                    nodeC.cap_remain=nodeF.cap_remain*rate;//更新各子节点的剩余空间
                    temp+=nodeC.cap_remain;
                }

                else
                    nodeC.cap_remain=nodeF.cap_remain-temp;//更新子节点的剩余空间
                double cap_min=nodeF.capacity*0.4-nodeC.num_current;//节点最小剩余容量=节点的存量最小值-当前存量
                nodeC.cap_remain=Math.max(nodeC.cap_remain, cap_min);
                if(node.type_dir==3)//无容量限制
                    nodeC.cap_remain=nodeC.supplys_in_acc[this.time_now_index];
            }

        }

    }
    ///////////////////
    //节点流出量和流入节点的流入量计算,并更新流入量、流出量和存量
    public void updateFlowofNode(Node node){

        if(node.type==6)
            node.num_current=Math.max(node.num_current, node.supplys_out_acc[this.time_now_index]);

        if(node.type==5&&node.status[this.time_now_index]!=2)//列车节点没有列车乘客下车事件
            return;
        double flowOut_sum=0;
        if(node.relOut==null)
            return;
        double temp=0;
        for(int i=0;i<node.relOut.length;i++){
            //遍历流出关系节点
            Node nodeTo=node.relOut[i];
            if(nodeTo.type==5&&nodeTo.status[this.time_now_index]!=1)//流向列车节点，则没有在乘客上车时间
            {nodeTo.flow_ins[this.time_now_index]=0;
                continue;
            }

            double supply_flow=0;
            if(i==node.relOut.length-1)
                supply_flow=node.supplys_out_acc[time_now_index]-temp;
            else
            {
                supply_flow=node.supplys_out_acc[time_now_index]*node.rateOut[i];//对该方向的供给
                temp+=supply_flow;
            }
            double supply_sum=nodeTo.supplys_in_acc[this.time_now_index];//接受方向的被供给总量
            double remain=nodeTo.cap_remain;//
            double inCap=nodeTo.flow_in_rate[this.time_now_index]*this.time_interval/1000*nodeTo.weight;

            double need=Math.min(remain, inCap);//该方向的消费量=min(容量-存量，流率*时间*宽度），即流入量

            double supply_need=0;
            //	if(node.id==7&&this.time_now_index==3747)
            //	System.out.println("supply_flow"+supply_flow+"remain:"+remain+"incap:"
            //		+inCap+"supply_sum"+supply_sum);
            double flowOut=0;
            if(supply_sum!=0) {
                flowOut=supply_flow*Math.min(1,need/supply_sum);//该方向的流出量=min(供给，供给*(消费)/供给总量），
            }
            if(node.type==5)//下车客流，下车优先
                flowOut=supply_flow;
//			double flowOut=Math.min(supply_flow, supply_need);
            nodeTo.flow_ins[this.time_now_index]+=flowOut;
            flowOut_sum+=flowOut;
        }
        node.flow_outs[this.time_now_index]=flowOut_sum;
        node.num_current-=node.flow_outs[this.time_now_index];//更新存量
        //if(node.capacity-node.num_current<0)
        //System.out.println("node.capacity-node.num_current:"+node.id+"--"+(node.capacity-node.num_current));
        node.cap_remain=Math.max(0, node.capacity-node.num_current);//更新剩余容量

    }
    /*
     * public void updateFlowofNode(Node node,int dir){//更新某个流入方向的流量 //但只能在列车停站时流入流出
     * if(node.type==5&&node.status[this.time_now_index]==0)//列车节点没有列车到达 return;
     * double flowOut_sum=0; if(node.relOut==null) return; //for(int
     * i=0;i<node.relOut.length;i++){ //遍历流出关系节点 Node nodeTo=node.relOut[dir];
     * if(nodeTo.type==5&&nodeTo.status[this.time_now_index]==0)//列车节点没有列车到达 return;
     * double
     * supply_flow=node.supplys_out_acc[time_now_index]*node.rateOut[dir];//对该方向的供给
     * double supply_sum=nodeTo.supplys_in_acc[this.time_now_index];//接受方向的被供给总量
     * double remain=nodeTo.cap_remain;// double
     * inCap=nodeTo.flow_in_rate[this.time_now_index]*this.time_interval/1000*nodeTo
     * .weight; if(node.type==5)//下车客流 remain=inCap; double need=Math.min(remain,
     * inCap);//该方向的消费量=min(容量-存量，流率*时间*宽度），即流入量 double supply_need=0;
     * if(node.id==7&&this.time_now_index==3747)
     * System.out.println("supply_flow"+supply_flow+"remain:"+remain+"incap:"
     * +inCap+"supply_sum"+supply_sum); if(supply_sum!=0)
     * supply_need=supply_flow*need/supply_sum;//该方向的流出量=min(供给，供给*(消费)/供给总量），
     * double flowOut=Math.min(supply_flow, supply_need);
     * nodeTo.flow_ins[this.time_now_index]+=flowOut; //flowOut_sum+=flowOut; //}
     * node.flow_outs[this.time_now_index]+=flowOut; }
     */
    ///////////////////
    //节点状态值更新

    public void updateValueofNode1(Node node){
        //更新非父节点存量，和等候的流入流出量、下一时刻的供给流出量
        if(node.type_dir==2)//父节点
            return;
        node.num_current+=node.flow_ins[this.time_now_index];
		/*//node.num_current-=node.flow_outs[this.time_now_index];//更新存量
		if(node.type==5) {
			node.cap_remain-=node.flow_ins[this.time_now_index];
			if(node.cap_remain<0)
				System.out.println("node.cap_remain:"+(node.cap_remain));
		}*/

        //else
        {
            node.cap_remain=Math.max(0, node.capacity-node.num_current);//更新剩余容量
            //if(node.capacity-node.num_current<0)
            //System.out.println("node.capacity-node.num_current2:"+node.id+":"+(node.capacity-node.num_current));
        }
        //node.cap_remain=Math.max(0, node.capacity-node.num_current);//更新剩余容量
        node.wait_nums_in[this.time_now_index]=node.supplys_in_acc[this.time_now_index]-node.flow_ins[this.time_now_index];;
        node.wait_nums_out[this.time_now_index]=Math.max(0,node.supplys_out_acc[this.time_now_index]-node.flow_outs[this.time_now_index]);//更新当前时刻的等待人数（供给-流出），更新当前节点状态，
        int time_next=time_now_index+1;
        if(time_number_index>time_next){
            node.supplys_out_acc[time_next]+=node.wait_nums_out[this.time_now_index];//下一个时刻的supplys_out增加wait_nums_out；
            //node.supplys_in_acc[time_next]+=node.wait_nums_in[this.time_now_index];//下一个时刻的supplys_in增加？减去wait_nums_in;
        }
        node.number[this.time_now_index]=node.num_current;
    }
    public void updateValueofNodeF(Node node){
        //更新父节点当前存量为子节点存量之和，平均密度，等候的流入流出量？下一时刻的供给流出量？通过速度?、通过时间?
        node.num_current=0;
        for(int i=0;i<node.children.length;i++){
            Node child=node.children[i];
            node.num_current+=child.num_current;
            node.wait_nums_in[this.time_now_index]+=child.wait_nums_in[this.time_now_index];
            node.wait_nums_out[this.time_now_index]+=child.wait_nums_out[this.time_now_index];
        }
        node.cap_remain=Math.max(0,node.capacity-node.num_current);//更新剩余容量
        node.dens_average[this.time_now_index]=node.num_current/node.area;//根据存量和面积更新当前时刻的平均密度，
        node.number[this.time_now_index]=node.num_current;
    }
    public void updateValueofNode2(Node node){
        //更新非父节点（普通节点和子节点）的平均密度（子节点同父节点）通过速度、通过时间和未来供给

        if(node.type_dir==2)//父节点
            return;
        if(node.type==5||node.type==6)
            return;
        double numberWalk=0;

        if(node.type_dir==1)//子节点
            numberWalk=node.father.num_current;//-node.wait_nums_out[this.time_now_index]; //行走人数
        else
            numberWalk=node.num_current;//-node.wait_nums_out[this.time_now_index]; //行走人数
        node.dens_average[this.time_now_index]=numberWalk/node.area;//根据存量和面积更新当前时刻的平均密度，
        if(this.time_now_index+1<this.time_number_index)
            updateFlowInRate(node);//根据当前的走行人数，更新下一个节拍的流率

        //根据密度和节点状态更新当前时刻的速度和流率，''
        //if(node.type!=5&&node.type!=6)//非列车节点和进站节点
        {

            double len=node.length;
            //根据速度和长度更新当前时刻的通过时间，
            if(this.time_now_index+1<this.time_number_index&&node.velocitys[this.time_now_index+1]!=0){

                node.time_passs[this.time_now_index]=(long)(len/node.velocitys[this.time_now_index+1]);

                //if(this.bControl>1)
                //for(int i=0;i<this.nodes_control_lenth.length;i++){
			/*if(node.id==nodes_control_lenth[i]&&this.time_now<this.time_end_control2&&
					this.time_now>=this.time_start_control2)
				len+=this.control_second[i];*/
                //node.time_passs[this.time_now_index]=(long)(len/node.velocitys[this.time_now_index]);
                //node.time_passs[this.time_now_index]+=this.control_second[i];
                //}

/*			if(node.id==nodes_control_lenth[i]&&this.time_now<this.time_end_control2&&
					this.time_now>=this.time_start_control2)
				System.out.println(this.time_now_index+"control 3:"+node.id+":"+node.time_passs[this.time_now_index]+"+"
						+this.control_second[i]);
				node.time_passs[this.time_now_index]+=this.control_second[i];
			}*/
            }
            //根据通过时间计算未来时刻，更新未来供给，
            long time_future_index=(((long)node.time_passs[this.time_now_index]*1000+this.time_now-this.time_start)/this.time_interval);//何时更新未来供给，流入时更新 后续时刻的新供给和累计供给，累计供给在流出时二次更新
				/*if(time_future_index<0){
					System.out.println("time_future_index:"+time_future_index+
							"node.time_passs[this.time_now_index]"+node.time_passs[this.time_now_index]+"v:"+node.velocitys[this.time_now_index]+
					      "len:"+len+"control_length:"+ control_length[0] +
					      "nodes_control_lenth:"+node.id);
				}
*/				if(time_future_index==this.time_now_index)
            time_future_index++;//对闸机等快速通过类型设施认为在下一个节拍流出
            if(time_future_index<this.time_number_index){
                node.supplys_out_acc[(int)time_future_index]+=node.flow_ins[this.time_now_index];
                node.supplys_out_new[(int)time_future_index]+=node.flow_ins[this.time_now_index];
            }
            //	node.num_current=Math.max(node.num_current, node.supplys_out_acc[this.time_now_index]);
            //node.cap_remain=node.capacity-node.num_current;//更新剩余容量

            updateWaitTimeofNode(node);//更新节点分时等待时间

        }

    }
    //更新父节点的未来供给？

    /*	public void updateValuesofNode(Node node){
            //对于列车类型的节点不更新supplys_out_acc和supplys_out_new
            //根据节点类型需要
            //if(node.type==6)//增加判断，如果当前节点为进站节点
            node.num_current=Math.max(node.num_current, node.supplys_out_acc[this.time_now_index]);
            node.num_current+=node.flow_ins[this.time_now_index];
            node.num_current-=node.flow_outs[this.time_now_index];//更新存量
            node.cap_remain=node.capacity-node.num_current;//更新剩余容量
            node.wait_nums_in[this.time_now_index]=node.supplys_in_acc[this.time_now_index]-node.flow_ins[this.time_now_index];;
            node.wait_nums_out[this.time_now_index]=node.supplys_out_acc[this.time_now_index]-node.flow_outs[this.time_now_index];//更新当前时刻的等待人数（供给-流出），更新当前节点状态，
            int time_next=time_now_index+1;
            if(time_number_index>time_next){
                node.supplys_out_acc[time_next]+=node.wait_nums_out[this.time_now_index];//下一个时刻的supplys_out增加wait_nums_out；
                //node.supplys_in_acc[time_next]+=node.wait_nums_in[this.time_now_index];//下一个时刻的supplys_in增加？减去wait_nums_in;
            }
            node.dens_average[this.time_now_index]=node.num_current/node.area;//根据存量和面积更新当前时刻的平均密度，
            //根据密度和节点状态更新当前时刻的速度和流率，''
            if(node.type!=5)
                {
                computeFlowInRate(node);

            //根据速度和长度更新当前时刻的通过时间，
            if(node.velocitys[this.time_now_index]!=0){
                node.time_passs[this.time_now_index]=(long)(node.length/node.velocitys[this.time_now_index]);

            }
        }

            //根据通过时间计算未来时刻，更新未来供给，
            int time_future_index=(int)((node.time_passs[this.time_now_index]*1000+this.time_now-this.time_start)/this.time_interval);//何时更新未来供给，流入时更新 后续时刻的新供给和累计供给，累计供给在流出时二次更新
            if(time_future_index==this.time_now_index)
                time_future_index++;//对闸机等快速通过类型设施认为在下一个节拍流出
            if(node.type!=5&&time_future_index<this.time_number_index){
                node.supplys_out_acc[time_future_index]+=node.flow_ins[this.time_now_index];
                node.supplys_out_new[time_future_index]+=node.flow_ins[this.time_now_index];
            }
        //	node.num_current=Math.max(node.num_current, node.supplys_out_acc[this.time_now_index]);
            //node.cap_remain=node.capacity-node.num_current;//更新剩余容量

            updateWaitTimeofNode(node);//更新节点分时等待时间
        }*/
    /////////////////////
    //统计节点等待人时 后续增加
    public void updateWaitCostofNode(Node node){
        //每个节拍结束时，节点总等待人时=(供给-流出)*节拍

    }
    ///////////////////
    //节点平均等待时间计算,后续增加
    public void ComputewaitAverofNode(Node node){
        //对节点统计的总等待人时，除以节点的总的供给人数
    }
    /////////////////////////
    ////计算流线的平均通过时间 后续增加
    public void ComputeTimeofLine(Node node){
        //循环并累加流程途径节点的平均等待时间
    }
    /////////////////////
    //统计车站的乘客平均等待时间
    public double ComputewaitAverofStation(){
        //对于所有有节点，统计总人数sumNum，和总的等待人时sumwaitNum，相除
        //总的等待人时计算：所有节点每个时段的实际等待人数之和，
        //总人数为所有的进站人数+所有的下车人数16726+70559
        int sumNum=16726+78347;
        double wait_number=0;
        double sumWaitNum=0;
        //double refnum1=1500,refnum2=2000;
        for(int i=0;i<this.nodes.length;i++){
            Node node=this.nodes[i];

            if(node.type==10)
                continue;
            for(int j=0;j<node.wait_nums_out.length;j++){/*
				if(node.id==73||node.id==74||node.id==75||node.id==76){
					if(wait_number<(node.supplys_in_acc[j]+node.supplys_out_acc[j]))
						wait_number=(node.supplys_in_acc[j]+node.supplys_out_acc[j]);
				}*/
                if(node.type==2&&node.type_dir==0) {
                    //	System.out.println("ok");
                    double temp=node.number[j]-node.cap_safe;
                    //if(temp>200)System.out.println(node.id+node.describe+node.number[j]+":");
                    //System.out.println(node.number[j]);
                    if(wait_number<temp){
                        wait_number=temp;
                    }
                }
                if(node.type!=5)
                    sumWaitNum+=node.wait_nums_out[j];

            }
        }
        for(int i=0;i<this.nodes_father.length;i++){
            Node node=this.nodes_father[i];

            if(node.type==10)
                continue;
            for(int j=0;j<node.wait_nums_out.length;j++){/*
				if(node.id==73||node.id==74||node.id==75||node.id==76){
					if(wait_number<(node.supplys_in_acc[j]+node.supplys_out_acc[j]))
						wait_number=(node.supplys_in_acc[j]+node.supplys_out_acc[j]);
				}*/
                if(((node.type==1||node.type==0)&&node.type_dir==2)//站台、站厅
                        ||(node.type==2&&node.type_dir==0))//通道
                {
                    if(node.type==2&&node.type_dir==0)
                        System.out.println("ok");
                    double temp=node.number[j]-node.cap_safe;
                    //if(temp>200)System.out.println(node.id+node.describe+node.number[j]+":");
                    //System.out.println(node.number[j]);
                    if(wait_number<temp){
                        wait_number=temp;
                        //System.out.println(node.id+node.describe+node.number[j]+":");
                    }

                }


            }
        }
        double wait_aver=sumWaitNum/sumNum;
//        this.solution.wait_aver=wait_aver;
//        this.solution.ex_number=wait_number;//更新solution中对应的内容
        return wait_aver;

    }
    public double ComputeStayAverofStation(){//计算平均行程时间
        //对于所有有节点，统计总人数sumNum，和总的等待人时sumwaitNum，相除
        //总的人时计算：所有节点每个时段的实际等待人数之和，进站节点和列车节点除外的流出人数*通过时间
        //总人数为所有的进站人数+所有的下车人数16726+70559
        //int sumNum=13361+83132;//16726+78347;
        double sumWaitNum=0;
        double sumPassNum=0;
        double sumTime=0;
        double sumOut=0;
        double sumEnd=0;
        double sumOut2=0;
        for(int i=0;i<this.nodes.length;i++){
            Node node=this.nodes[i];
            if(node.type==10)
                //for(int j=0;j<node.wait_nums_out.length;j++){
                sumOut2+=node.number[node.wait_nums_out.length-1];
            //}
            if(node.type==10||node.type==5)//消失点不计算
                continue;
            for(int j=0;j<node.wait_nums_out.length;j++){
                sumWaitNum+=node.wait_nums_out[j];
                if(node.type!=5&&node.type!=6)//列车和进站节点不计
                    sumPassNum+=node.flow_outs[j]*node.time_passs[j];
                sumTime+=node.number[j];
                if(node.type==7)
                    sumOut+=node.flow_outs[j];
                if(node.type==101)
                {
                    sumOut+=node.flow_outs[j];
                    sumOut2+=node.flow_outs[j];
                }
                if(j==node.wait_nums_out.length-1)
                    sumEnd+=node.number[j];
            }
        }
        //System.out.println("sumEnd:"+sumEnd+" sumOut:"+sumOut+" sumOut2:"+sumOut2+" sum:"+(sumEnd+sumOut)
        //	+"sumNum:"+sumNum);
        double server_aver=(sumTime)/sumNum;
        //double server_aver=(sumWaitNum+sumPassNum)/sumNum;
//        this.solution.server_aver=server_aver;
        return server_aver;

    }
    ////////////////////////
    //统计分时的节点流量等待时间
    public void updateWaitTimeofNode(Node node){
        //	int time_out_index=0;//上一次流出的供给时刻t0的索引
        //public double passengers_out=0;//累计流出人数
        //public double passenger_out_temp=0;//供给时刻t0流出的人数
        //更新较早时刻到达，在该时刻流出的节点
        double out_remian=node.flow_outs[this.time_now_index];//剩余流出人数=流出人数
        int t=node.time_out_index;//t=t0;
        double out_temp=0;//临时流出人数;
        while(out_remian>0&&t<this.time_number_index)//剩余流出人数>0
        {
            double supply=node.supplys_out_new[this.time_now_index];//供给人数=t时刻的new供给人数?
            if(t==node.time_out_index)
                supply-=node.passenger_out_temp;//供给人数=节点new供给人数-t0时刻节点流出人数;
            out_temp=Math.min(supply, out_remian);//临时流出人数=Min（供给人数，剩余流出人数）；

            if(t==node.time_out_index)
                node.wait_time_out[t]=(node.wait_time_out[t]*node.passenger_out_temp+
                        out_temp*(this.time_now_index-t))/(node.passenger_out_temp+out_temp);
                //t时刻节点的等待时间=(t时刻节点的等待时间*t时刻节点流出人数+临时流出人数*（当前时刻-t))/(t时刻节点流出人数+临时流出人数)
            else
                node.wait_time_out[t]=out_temp*(this.time_now_index-t)/out_temp;
            //t时刻节点的等待时间=临时流出人数*（当前时刻-t))/(临时流出人数)?
            out_remian-=out_temp;//剩余流出人数-=临时流出人数
            t++;

        }
        node.time_out_index=t;//t0=t;
        node.passenger_out_temp=out_temp;//t0时刻new供给中的流出人数=临时流出人数；

    }


    //////////////////////////
    ////计算某时刻进站的乘客通过流线的时间
//    public double ComputeTimeofLine(long time,Line line){
//        //遍历节点，记录到达节点的时刻，对每个节点找到对应时刻的通过时间和等待时间
//        int t=this.getindexofTime(time);
//        double time_wait=0;
//        for(int i=0;i<line.nodes.length;i++){
//            Node node=line.nodes[i];
//            time_wait+=node.time_passs[t];
//            time+=node.time_passs[t];//流倒节点末端的时刻
//            t=this.getindexofTime(time);
//            time_wait+=node.wait_time_out[t];//t时刻到达节点末端需要的等待流出时间
//            time+=node.wait_time_out[t];//流出节点的时刻
//            t=this.getindexofTime(time);
//        }
//        return time_wait;
//    }
//
//    ////////////////////////////
//    //统计分时的流线通过时间
//    public void ComputeTimeofLines(){
//        int len=this.time_number_index;//分时刻；
//        for(int i=0;i<len;i++){
//
//            long time=this.getTimeofIndex(i);
//            for(int j=0;j<this.lines.length;j++){
//                //分流线；
//                Line line=this.lines[j];
//                line.time_pass[i]=ComputeTimeofLine(time,line);
//            }
//
//        }
//
//    }
    ////////////////////////
    ////计算所有乘客的平均等待时间,后续增加
    public void ComputeWaitAverofP(){
        //对每个节点的统计等待人时，再累加计算所有节点的等待人时总和；再除以所有节点的供给总人数，即车站乘客接受服务的平均等待时间

    }

    public void InitValuesofNode(Node node){//此部分后续增加，仿真初始的节点状态，
        //从开始运营初始时仅初始化客流源的供给，这在其他函数完成
        //根据节点类型需要
        //更存量，
        //更新当前时刻的等待人数（供给-流出），更新当前节点状态，
        //根据存量和面积更新当前时刻的平均密度，
        //根据密度和节点状态更新当前时刻的速度和流率，
        //根据速度和长度更新当前时刻的通过时间，
        //根据通过时间计算未来时刻，更新未来供给，

    }
    /////////////////////////
    //流率计算
    public void updateFlowInRate(Node node){
        //使用速度-密度关系和流率-密度关系计算
        //double density=Math.max(0,(node.num_current-node.wait_nums_out[this.time_now_index]))/node.area;//当前密度=(当前存量-等候人数)/面积
        node.velocitys[this.time_now_index+1]=this.getVfromD(node);//Math.min(node.v_limit,this.getVfromD(node));//密度速度关系
        //node.velocitys[this.time_now_index+1]=Math.min(node.v_limit,this.getVfromD(node));//密度速度关系
        //double fv=node.velocitys[this.time_now_index+1]*node.dens_average[this.time_now_index];//*node.weight;//计算流率;
        /*
         * if(node.velocitys[this.time_now_index+1]<2)//node.v_limit)//速度随密度下降 {
         * //double fv=node.velocitys[this.time_now_index+1]*node.dens_average[this.
         * time_now_index];//*node.weight;//计算流率;
         * if(node.flow_in_rate[this.time_now_index+1]>fv)
         * node.flow_in_rate[this.time_now_index+1]=fv; }
         */

        //if(this.time_now_index+1<this.time_number_index){
        //	//node.velocitys[this.time_now_index+1]=this.getVfromD(node);//密度速度关系
        //	if(node.velocitys[this.time_now_index]<node.v_limit)//速度随密度下降，流入不受速度密度关系影响
        //node.flow_in_rate[this.time_now_index+1]=node.velocitys[this.time_now_index]*density;//*node.weight;//计算流率
        //}
        //58号节点流入限流，7：30-9：00每小时1200，
        //3个控制点


        if((this.bControl==1))//&&this.time_now>=this.time_start_control[0]
        //&&this.time_now<=this.time_end_control[0]))//||(this.bControl==2&&this.time_now>=this.time_start_control[1]
        //&&this.time_now<=this.time_end_control[1])
        //	||(this.bControl==3&&this.time_now>=this.time_start_control[1]
        //			&&this.time_now<=this.time_end_control[1]))
        {
            for(int i=0;i<2;i++){
                if(node.id==nodes_control_in[i]){
                    int index=(int)((this.time_now-this.time_start)/((time_end-time_start)/control_in_number[i].length));
                    //node.flow_in_rate[this.time_now_index]=
                    //	this.control_in_number[i]/3600*this.time_interval/1000;
                    if(this.time_now>=this.time_start_control[i]&&this.time_now<=this.time_end_control[i]){
                        node.flow_in_rate[this.time_now_index+1]=
                                ((double)this.control_in_number[i][index])/node.weight*this.time_interval/1000;
                    }
                }
            }

        }
        if(this.bControl==2){
            for(int i=0;i<4;i++){
                if(this.time_now>=this.time_start_control[i]&&this.time_now<=this.time_end_control[i])
                {

                    if(node.id==nodes_control_in[i]){
                        int index=(int)((this.time_now-this.time_start)/((time_end-time_start)/control_in_number[i].length));
                        //node.flow_in_rate[this.time_now_index]=
                        //	this.control_in_number[i]/3600*this.time_interval/1000;
                        if(this.time_now_index+1<this.time_number_index){
                            node.flow_in_rate[this.time_now_index+1]=
                                    ((double)this.control_in_number[i][index])/node.weight*this.time_interval/1000;
                        }
                    }
                }
            }
        }
    }
    ///////////////////////
    //
    ///////////////////
    //车站统计指标： 后续


    ///////////////////
    //输出统计指标
    public void writeNode(Node node){
        //	分时输出某节点的分时密度、
        //其他：流量、通过时间、等待流出人数
        //按时间循环
        //写 密度、流入、流出、等待流出、通过时间
        double maxwaitin=0;
        try{
            FileWriter fileWriter1 = null;
            fileWriter1=new FileWriter("Data\\out\\"+node.id+".txt",false);//清空原有文件新写入		for(int i=0;i<real.length;i++){
            fileWriter1.write("时间\t存量\t密度\t速度\t供给流入量\t供给流出量\t流入量\t流出量"
                    + "\t通过时间\t等待流出人数\tflow_in_rate\n");
            int len=this.time_number_index;//分时刻；
            //Node node=this.nodes[ID];
            for(int i=0;i<len;i++){
                long time=this.getTimeofIndex(i);
                Date date=new Date();
                date.setTime(time);
                String timeStr=date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                fileWriter1.write(timeStr+"\t"+node.number[i]+"\t"+node.dens_average[i]+"\t"+node.velocitys[i]+"\t"+node.wait_nums_in[i]+"\t"
                        +node.supplys_out_acc[i]+"\t"+node.flow_ins[i]+"\t"+
                        node.flow_outs[i]+"\t"+node.time_passs[i]+"\t"+node.wait_nums_out[i]+"\t"+
                        node.flow_in_rate[i]+"\n");
                if(node.wait_nums_in[i]>maxwaitin)
                    maxwaitin=node.wait_nums_in[i];
            }
            fileWriter1.flush();
            fileWriter1.close();
		/*if(maxwaitin>30)
			System.out.println("maxwaitin:"+maxwaitin+"-node:"+node.id);*/
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void writeNodeFlow(Node node, int interval){//以五分钟为间隔统计流量,5分钟为300秒
        //记录时间，累加流量，当时间整除interval时写流量
        try{
            FileWriter fileWriter1 = null;
            fileWriter1=new FileWriter("Data\\out\\"+node.id+"flow.txt",false);//清空原有文件新写入		for(int i=0;i<real.length;i++){
            fileWriter1.write("时间\t分时流入量\t分时流出量\n");
            int len=this.time_number_index;//分时刻；
            //Node node=this.nodes[ID];
            double flow_in=0,flow_out=0;
            for(int i=0;i<len;i++){
                flow_in+=node.flow_ins[i];
                flow_out+=node.flow_outs[i];
                long time=this.getTimeofIndex(i);
                Date date=new Date();
                date.setTime(time);
                int time_temp=date.getMinutes()*60+date.getSeconds();
                if(time_temp%interval==0){
                    String timeStr=date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                    fileWriter1.write(timeStr+"\t"+flow_in+"\t"+flow_out+"\n");
                    flow_in=0;
                    flow_out=0;
                }
            }
            fileWriter1.flush();
            fileWriter1.close();

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void writeNodeAll(){
        for(int i=0;i<this.nodes.length;i++){
            Node node=nodes[i];
            this.writeNode(node);
            this.writeNodeFlow(node, 300);
        }
        for(int i=0;i<this.nodes_father.length;i++){
            Node node=nodes_father[i];
            this.writeNode(node);
        }
    }
//    public void writeLineAll(){
//        for(int i=0;i<this.lines.length;i++){
//            this.writeLine(i);
//        }
//    }
//    public void writeLine(int ID){
//        //	分时输出各流线的走行时间
//        //按时间循环
//        //写流线走行时间
//        try{
//            Line line=this.lines[ID];
//            FileWriter fileWriter1 = null;
//            fileWriter1=new FileWriter("Data\\out\\"+ID+"-"+line.type+".txt",false);//清空原有文件新写入		for(int i=0;i<real.length;i++){
//            fileWriter1.write("时间\t走行时间\n");
//            int len=this.time_number_index;//分时刻；
//            for(int i=0;i<len;i++){
//                long time=this.getTimeofIndex(i);
//                Date date=new Date();
//                date.setTime(time);
//                String timeStr=date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
//                fileWriter1.write(timeStr+"\t"+line.time_pass[i]+"\n");
//            }
//            fileWriter1.flush();
//            fileWriter1.close();
//
//        }
//        catch(IOException e)
//        {
//            e.printStackTrace();
//        }
//
//    }
    //{区域编号,区域长度,区域宽度,区域面积,区域容量,区域流量限制,区域速度限制,动态速度,动态密度,实际流量,计算流量，区域存量，计算时存量,并列设施客流比例,流出关系}

    public Station() {
        // TODO 自动生成的构造函数存根
    }
    public double getVfromD(int type,double d){//速度、密度关系

        double v=Math.max(0,-Math.pow(d,3)*0.03735+Math.pow(d,2)*0.2462-d*0.63+0.9753);
        return v;

    }
    public double getVfromD(Node node){//速度、密度关系
        double d=node.dens_average[this.time_now_index];//(node.num_current-node.wait_nums_out[this.time_now_index])/node.area;//当前密度=(当前存量-等候人数)/面积
        //根据node的类别进行计算
        double v=0;//Math.max(0,-Math.pow(d,3)*0.03735+Math.pow(d,2)*0.2462-d*0.63+0.9753);
		/*0	站厅
		1	站台
		2	通道
		3	混行楼梯
		4	扶梯
		5	列车
		6	进站口
		7	出站口
		8	进站点
		9	出站点
		10	消失点
		11	混行楼梯上行
		12	混行楼梯下行
		13	单行楼梯上行
		14	单行楼梯下行
		15	进出站口（父节点）
		16	虚拟节点	*/
        switch(node.type){
            case 13://楼梯上行
                v=Math.max(0.1,this.calUpstairv(d));//;
                break;
            case 14://楼梯下行
                v=Math.max(0.1,calDownstairv(d));//...;
                break;
            case 11://混行楼梯上行
                //	d=(node.father.num_current-node.wait_nums_out[this.time_now_index])/node.area;//当前密度=(当前存量-等候人数)/面积
                v=Math.max(0.1,this.calUpstairv(d));//...;
                break;
            case 12://混行楼梯下行
                //	d=(node.father.num_current-node.wait_nums_out[this.time_now_index])/node.area;//当前密度=(当前存量-等候人数)/面积
                v=Math.max(0.1,calDownstairv(d));//...;
                break;
            case 2://通道
            case 1://站台
            case 101://站台
            case 0://站厅
                v= Math.max(0.1,calPv(d));//...;
                break;
            default://扶梯、闸机等速度等于上限
                v=node.v_limit;//...;
                break;
        }

        return Math.min(v,node.v_limit);
        //return v;
    }


    /*public double calUpstairv(double x) {//楼梯上行速密关系
        double v=-Math.pow(x,3)*0.03159+Math.pow(x,2)*0.2028-x*0.5332+0.9522;
        return v;
    }

    public double calDownstairv(double x) {//楼梯下行速密关系
        double v=-Math.pow(x,3)*0.0308+Math.pow(x,2)*0.1809-x*0.459+0.9397;
        return v;
    }

    public double calPv(double x) {//通道速密关系
        double v=-Math.pow(x,4)*0.02955+Math.pow(x,3)*0.1561-Math.pow(x,2)*0.1998-x*0.4865+1.484;
        return v;
    }*/
    public double calUpstairv(double d) {//上行楼梯速密关系
        double v=-Math.pow(d,3)*0.03735+Math.pow(d,2)*0.2462-d*0.63+0.9753;
        return v;
    }

    public double calDownstairv(double d) {//下行楼梯速密关系
        double v=-Math.pow(d,3)*0.06727+Math.pow(d,2)*0.3257-d*0.6372+1.008;
        return v;
    }


    public double calPv(double d) {//通道速密关系
        //double v=Math.pow(d,3)*0.03302-Math.pow(d,2)*0.06169-d*0.5186+1.481;
        double v=-Math.pow(d,4)*0.02955+Math.pow(d,3)*0.1561-Math.pow(d,2)*0.1998-d*0.4865+1.484;
        return v;
    }


	/*public double calflow1(double a,double b,double c) {//调用速度密度关系计算 根据区域密度计算区域内客流速度
		double y =a*b*c;
		return y;
	}

	public int calGetoff(int x) {//调用速度密度关系计算 根据区域密度计算区域内客流速度
		int y =(int)((1470*1.2-1470*1.2+x)/10);
		return y;
	}
	*/

}
