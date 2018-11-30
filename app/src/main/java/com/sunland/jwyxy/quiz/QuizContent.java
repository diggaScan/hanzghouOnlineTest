package com.sunland.jwyxy.quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizContent {
    public static String[] titles={"金融寡头政治上的统治主要是通过","垄断组织在采购原材料时多采取",
            "垄断资本条件下垄断企业竞争的目的是为了","国家垄断资本主义是","国家垄断资本主义的根本特征是",
    "马克思主义理论从狭义上说是","整个马克思主义学说的理论基础是","马克思主义生命力的根源在于","学习马克思主义的根本方法是"
    ,"对待马克思主义的科学态度是"};
    public static int[] answers={2,2,3,1,3,3,1,1,4,3};
    public static List<String[]> choice =new ArrayList<>();
    public static String[] one={"合作参与制", "个人联合", "建立政策研究咨询机构对政府施加影响","掌握舆论工具控制新闻媒介利润"};
    public static String[] two={"垄断高价", "垄断低价","自由价格","市场价格"};
    public static String[] three={"获得平均利润","获得超额利润","获得高额垄断利润","消灭中小企业"};
    public static String[] four={"国家政权与垄断资本相结合的垄断资本主义","国家政权与垄断资本相分离的垄断资本主义","消除了生产无政府状态的垄断资本主义","解决了资本主义基本矛盾的垄断资本主义"};
    public static String[] five={"国有企业的存在","国私共有的垄断资本","私人垄断资本的主导地位","国家对经济的干预和调节"};
    public static String[] six={"无产阶级争取自身解放和整个人类解放得学说体系",
            "关于无产阶级的斗争得性质、目的和解放条件的学说",
    "马克思和恩格斯创立的基本理论、基本观点和基本方法构成的科学体系",
    "关于资本主义发展和转变为社会主义以及社会主义和共产主义发展的普遍规律的学说" };
    public static String[] seven={"马克思主义哲学", "马克思主义政治经济学",
    "科学社会主义学说", "阶级和阶级斗争学说" };
    public static String[] eight={"以实践性为基础的科学性与革命性的统一", "与时俱进","科学性与阶级性的统一","科学性" };
    public static String[] nine={"具体问题具体分析的方法", "辩证思维的方法",
    "实用主义的方法", "理论联系实际的方法" };
    public static String[] ten={"只坚持不发展","只发展不坚持",
    "既坚持又发展", "要记住马克思和恩格斯说过的每一句话" };
    static {
        choice.add(one);
        choice.add(two);
        choice.add(three);
        choice.add(four);
        choice.add(five);
        choice.add(six);
        choice.add(seven);
        choice.add(eight);
        choice.add(nine);
        choice.add(ten);
    }

}
