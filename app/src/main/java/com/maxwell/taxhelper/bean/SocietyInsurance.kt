package com.maxwell.taxhelper.bean

/**
 * Created by maxwellma on 03/11/2017.
 */
class SocietyInsurance() {

    var gr_shiye: Double = 0.005
    var gr_yanglao: Double = 0.08
    var gr_yiliao: Double = 0.02
    var gr_gongshang: Double = 0.0
    var gr_shengyu: Double = 0.0
    var gr_jichuzfgjj: Double = 0.07
    var gr_buchongzfgjj: Double = 0.0

    var gs_shiye: Double = 0.005
    var gs_yanglao: Double = 0.2
    var gs_yiliao: Double = 0.095
    var gs_gongshang: Double = 0.002
    var gs_shengyu: Double = 0.01
    var gs_jichuzfgjj: Double = 0.07
    var gs_buchongzfgjj: Double = 0.0

    var min_gjj: Double = 2190.0
    var min_shebao: Double = 3902.0
    var max_shebao: Double = 19512.0
    var max_gjj : Double = 19512.0

    constructor(gr_yanglao: Double, gr_yiliao: Double, gr_shiye: Double, gr_jichuzfgjj: Double, gr_buchongzfgjj: Double, gr_gongshang: Double, gr_shengyu: Double, gs_yanglao: Double, gs_yiliao: Double, gs_shiye: Double, gs_jichuzfgjj: Double, gs_buchongzfgjj: Double, gs_gongshang: Double, gs_shengyu: Double, min_shebao: Double, min_gjj: Double, max_shebao: Double, max_gjj: Double) : this() {
        this.gr_gongshang = gr_gongshang
        this.gr_buchongzfgjj = gr_buchongzfgjj
        this.gr_jichuzfgjj = gr_jichuzfgjj
        this.gr_shengyu = gr_shengyu
        this.gr_shiye = gr_shiye
        this.gr_yiliao = gr_yiliao
        this.gr_yanglao = gr_yanglao

        this.gs_buchongzfgjj = gs_buchongzfgjj
        this.gs_gongshang = gs_gongshang
        this.gs_jichuzfgjj = gs_jichuzfgjj
        this.gs_shengyu = gs_shengyu
        this.gs_shiye = gs_shiye
        this.gs_yanglao = gs_yanglao
        this.gs_yiliao = gs_yiliao

        this.min_gjj = min_gjj
        this.min_shebao = min_shebao
        this.max_gjj = max_gjj
        this.max_shebao = max_shebao
    }

    fun getParamsByCity(cityName: String): SocietyInsurance {
        when (cityName.removeSuffix("市")) {
            "上海" -> return SocietyInsurance(0.08, 0.02, 0.005, 0.07, 0.0, 0.0, 0.0, 0.2, 0.095, 0.005, 0.07, 0.0, 0.002, 0.01, 3902.0, 2190.0, 19512.0, 19512.0)
            "北京" -> return SocietyInsurance(0.08, 0.02, 0.002, 0.12, 0.0, 0.0, 0.0, 0.19, 0.1, 0.008, 0.12, 0.0, 0.004, 0.008, 3082.0, 2148.0, 23118.0, 23118.0)
            "南京" -> return SocietyInsurance(0.08, 0.02, 0.005, 0.08, 0.0, 0.0, 0.0, 0.20, 0.09, 0.01, 0.08, 0.0, 0.005, 0.005, 2628.0, 1770.0, 16200.0, 18200.0)
            "广州" -> return SocietyInsurance(0.08, 0.02, 0.002, 0.1, 0.0, 0.0, 0.0, 0.14, 0.08, 0.0064, 0.1, 0.0, 0.004, 0.0085, 2461.0, 1550.0, 12303.0, 26565.0)
            "深圳" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.13, 0.0, 0.0, 0.0, 0.11, 0.045, 0.02, 0.13, 0.0, 0.005, 0.007, 2336.0, 1500.0, 12615.0, 24588.0)
            "天津" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.11, 0.0, 0.0, 0.0, 0.2, 0.09, 0.02, 0.11, 0.0, 0.005, 0.008, 1720.0, 1160.0, 9380.0, 9380.0)
            "杭州" -> return SocietyInsurance(0.08, 0.02, 0.005, 0.12, 0.0, 0.0, 0.0, 0.14, 0.115, 0.1, 0.12, 0.0, 0.003, 0.1, 2585.95, 1860.0, 12929.76, 19717.0)
            "厦门" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.14, 0.08, 0.02, 0.08, 0.0, 0.005, 0.008, 1822.8, 10.0, 9114.0, 9114.0)
            "武汉" -> return SocietyInsurance(0.08, 0.02, 0.005, 0.08, 0.0, 0.0, 0.0, 0.20, 0.08, 0.015, 0.08, 0.0, 0.005, 0.007, 2599.0, 1300.0 ,12994.8, 22733.75)
            "长沙" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.20, 0.08, 0.02, 0.08, 0.0, 0.005, 0.007, 1500.0, 850.0, 7500.0, 7500.0)
            "太原" -> return SocietyInsurance(0.08, 0.02, 0.005, 0.06, 0.0, 0.0, 0.0, 0.2, 0.065, 0.015, 0.1, 0.0, 0.006, 0.005, 774.60, 774.60, 3873.0, 3873.0)
            "呼和浩特" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.2, 0.04, 0.02, 0.08, 0.0, 0.2, 0.007, 1015.20, 0.0, 5076.0, 5076.0)
            "石家庄" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.07, 0.0, 0.0, 0.0, 0.2, 0.08, 0.02, 0.11, 0.0, 0.005, 0.008, 1419.0, 0.0, 7095.0, 7095.0)
            "济南" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.2, 0.08, 0.02, 0.08, 0.0, 0.005, 0.008, 1556.0, 0.0, 7776.0, 7776.0)
            "苏州" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.2, 0.08, 0.02, 0.08, 0.0, 0.005, 0.01, 2387.0, 0.0, 16208.0, 15400.0)
            "福州" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.18, 0.08, 0.02, 0.08, 0.0, 0.005, 0.007, 1369.0, 0.0, 6845.0, 6845.0)
            "合肥" -> return  SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.2, 0.08, 0.01, 0.08, 0.0, 0.005, 0.007, 1489.2, 0.0, 7446.0, 7446.0)
            "青岛" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.2, 0.09, 0.01, 0.08, 0.0, 0.003, 0.007, 1270.0, 0.0, 6348.0, 6348.0)
            "南昌" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.2, 0.06, 0.02, 0.08, 0.0, 0.004, 0.008, 1522.0, 0.0, 7611.0, 7611.0)
            "郑州" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.2, 0.08, 0.02, 0.08, 0.0, 0.005, 0.01, 1492.0, 0.0, 8195.0, 8195.0)
            "南宁" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.2, 0.075, 0.01, 0.08, 0.0, 0.004, 0.006, 1283.0, 0.0, 6415.0, 6415.0)
            "海口" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.12, 0.0, 0.0, 0.0, 0.2, 0.07, 0.02, 0.12, 0.0, 0.01, 0.005, 1532.4, 0.0, 7662.0, 7662.0)
            "珠海" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.1, 0.0, 0.0, 0.0, 0.10, 0.06, 0.01, 0.1, 0.0, 0.002, 0.007, 1608.6, 0.0, 8043.0, 8043.0)
            "沈阳" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.19, 0.08, 0.02, 0.08, 0.0, 0.02, 0.008, 1929.0, 0.0, 9645.0, 9645.0)
            "长春" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.07, 0.0, 0.0, 0.0, 0.21, 0.07, 0.015, 0.07, 0.0, 0.005, 0.007, 1522.0, 0.0, 7611.0, 7611.0)
            "哈尔滨" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.22, 0.08, 0.01, 0.08, 0.0, 0.004, 0.005, 1276.0, 0.0, 6381.0, 6381.0)
            "西安" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.2, 0.07, 0.02, 0.08, 0.0, 0.005, 0.005, 1514.40, 0.0, 7572.0, 7572.0)
            "银川" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.20, 0.06, 0.02, 0.08, 0.0, 0.009, 0.007, 1839.6, 1839.6, 9198.0, 9198.0)
            "兰州" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.1, 0.0, 0.0, 0.0, 0.21, 0.06, 0.02, 0.1, 0.0, 0.005, 0.1, 1305.60, 0.0, 6528.0, 6528.0)
            "西宁" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.07, 0.0, 0.0, 0.0, 0.20, 0.06, 0.015, 0.07, 0.0, 0.1, 0.015, 929.4, 0.0, 4647.0, 4647.0)
            "乌鲁木齐" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.12, 0.0, 0.0, 0.0, 0.2, 0.075, 0.02, 0.12, 0.0, 0.005, 0.008, 740.40, 0.0, 3702.0, 3702.0)
            "重庆" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.07, 0.0, 0.0, 0.0, 0.18, 0.06, 0.01, 0.07, 0.0, 0.006, 0.006, 1178.0, 0.0, 8832.0, 8832.0)
            "成都" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.07, 0.0, 0.0, 0.0, 0.20, 0.075, 0.02, 0.07, 0.0, 0.006, 0.006, 1364.0, 0.0, 6819.0, 6819.0)
            "昆明" -> return SocietyInsurance(0.08, 0.02, 0.005, 0.1, 0.0, 0.0, 0.0, 0.2, 0.1, 0.01, 0.1, 0.0, 0.003, 0.005, 1449.0, 0.0, 7248.0, 7248.0)
            "贵阳" -> return SocietyInsurance(0.08, 0.02, 0.01, 0.08, 0.0, 0.0, 0.0, 0.18, 0.075, 0.02, 0.12, 0.0, 0.006, 0.007, 1230.0, 0.0, 6150.0, 6150.0)
            else -> return SocietyInsurance()
        }
    }
}