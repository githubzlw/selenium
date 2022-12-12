$(document).ready(function(){
    var keyWord = sessionStorage.getItem("keyWord");
    var strings = keyWord.split(";");
    for (var i = 0; i < strings.length; i++) {
        var string = strings[i];
        $("#u141").append('<button class="key_word" onclick="fn(\''+string+'\')">'+string+'</button>');
    }
    $("#u145_input").val(sessionStorage.getItem("asin_2"));
    f(JSON.parse(sessionStorage.getItem("data")));
    fn(strings[0]);
    ranking3(sessionStorage.getItem("time_start"),sessionStorage.getItem("time_end"),keyWord,sessionStorage.getItem("asin_2"),sessionStorage.getItem("campaignName"));
});

function f(data) {
    console.log(data)
    $("#table").html("");
    $("#table").append('<tr>\n' +
        '            <th>日期</th>\n' +
        '            <th>自然排名</th>\n' +
        '            <th>广告排名</th>\n' +
        '            <th>广告曝光量</th>\n' +
        '            <th>cpc出价</th>\n' +
        '            <th>广告订单量</th>\n' +
        '            <th>campaign name</th>\n' +
        '          </tr>');
    var xasix = [];
    var yasix1 = [];
    var yasix2 = [];
    var yasix3 = [];
    var yasix4 = [];
    var yasix5 = [];
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.date+'</td>\n' +
            '<td>'+map.amz_product_normal_count+'</td>\n' +
            '<td>'+map.amz_product_not_normal_count+'</td>\n' +
            '<td>'+map.Impressions+'</td>\n' +
            '<td>'+map.cpc+'</td>\n' +
            '<td>'+map.Day7_TotalOrders+'</td>\n' +
            '<td>'+map.Campaign_Name.substr(0,20)+'</td>\n' +
            '</tr>';
        xasix.push(map.date);
        if (map.amz_product_normal_count==0){
            yasix1.push('');
        }else {
            yasix1.push(map.amz_product_normal_count);
        }
        if (map.cpc==0){
            yasix2.push('');
        }else {
            yasix2.push(map.cpc);
        }
        if (map.Day7_TotalOrders==0){
            yasix3.push('');
        }else {
            yasix3.push(map.Day7_TotalOrders);
        }
        if (map.amz_product_not_normal_count==0){
            yasix4.push('');
        }else {
            yasix4.push(map.amz_product_not_normal_count);
        }
        if (map.Impressions==0){
            yasix5.push('');
        }else {
            yasix5.push(map.Impressions);
        }
        $("#table").append(html);
    }
    view(xasix,yasix1,yasix2,yasix3,yasix4);
    view1(xasix,yasix5,yasix2);
}

function ranking() {
    var keyWord = sessionStorage.getItem("keywName");
    var asin = $("#u145_input").val();
    var campaignName =sessionStorage.getItem("campaignName");
    if(null == asin || '' == asin){
        alert("asin不能为空，请按照格式输入")
        return;
    }
    var startDate = $("#u70_div").val();
    var endDate = $("#u72_div").val();
    request(startDate,endDate,asin,keyWord,campaignName);
}

function keySearch() {
    var e = event || window.event;
    if (e && e.keyCode == 13) { //回车键的键值为13
        ranking();
    }
}

function fn(keyWordName) {
    sessionStorage.setItem("keywName", keyWordName);
}

function view(xasix,yasix1,yasix2,yasix3,yasix4) {
    var dom = document.getElementById('container');
    var myChart = echarts.init(dom, null, {
        renderer: 'canvas',
        useDirtyRect: false
    });
    var app = {};

    var option;

    option = {
        legend: {
            data: ['广告订单数量', '自然排名', '广告排名', 'CPC出价'],
            bottom :10
        },
        xAxis: {
            type: 'category',
            name: '时间',
            data: xasix
        },
        yAxis: [
            {
                type: 'value',
                name: '搜索排名',
                position: 'left'
            },
            {
                type: 'value',
                name: '广告出价',
                position: 'right'
            }
        ],
        series: [
            {
                name: '广告订单数量',
                data: yasix3,
                yAxisIndex: 0,
                type: 'bar'
            },
            {
                name: '自然排名',
                data: yasix1,
                yAxisIndex: 0,
                type: 'line'
            },
            {
                name: '广告排名',
                data: yasix4,
                yAxisIndex: 0,
                type: 'line'
            },
            {
                name: 'CPC出价',
                data: yasix2,
                yAxisIndex: 1,
                type: 'line'
            }
        ],
        grid: {
            x:80,
            y:250
        }
    };

    if (option && typeof option === 'object') {
        myChart.setOption(option);
    }

    window.addEventListener('resize', myChart.resize);
}



function view1(xasix,yasix5,yasix2) {
    var dom = document.getElementById('container_1');
    var myChart = echarts.init(dom, null, {
        renderer: 'canvas',
        useDirtyRect: false
    });
    var app = {};

    var option;

    option = {
        legend: {
            data: ['广告曝光量',  'CPC出价'],
            bottom :10
        },
        xAxis: {
            type: 'category',
            name: '时间',
            data: xasix
        },
        yAxis: [
            {
                type: 'value',
                name: '广告曝光量',
                position: 'left'
            },
            {
                type: 'value',
                name: '广告出价',
                position: 'right'
            }
        ],
        series: [
            {
                name: '广告曝光量',
                data: yasix5,
                yAxisIndex: 0,
                type: 'line'
            },
            {
                name: 'CPC出价',
                data: yasix2,
                yAxisIndex: 1,
                type: 'line'
            }
        ],
        grid: {
            x:80,
            y:250
        }
    };

    if (option && typeof option === 'object') {
        myChart.setOption(option);
    }

    window.addEventListener('resize', myChart.resize);
}






function f2(data) {
    console.log(data)
    $("#table_1").html("");
    $("#table_1").append('<tr>\n' +
        '            <th>日期</th>\n' +
        '            <th>campagin name</th>\n' +
        '            <th>price</th>\n' +
        '            <th>广告花费</th>\n' +
        '            <th>广告销售额</th>\n' +
        '            <th>总销售额</th>\n' +
        '            <th>ACOS</th>\n' +
        '            <th>TACOS</th>\n' +
        '            <th>Session 数量</th>\n' +
        '            <th>Review 数量</th>\n' +
        '            <th>星级</th>\n' +
        '          </tr>');
    var xasix = [];
    var yasix1 = [];
    var yasix2 = [];
    var yasix3 = [];
    var yasix4 = [];
    var yasix5 = [];
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.date+'</td>\n' +
            '<td>'+map.Campaign_Name+'</td>\n' +
            '<td>'+map.price+'</td>\n' +
            '<td>'+map.spend+'</td>\n' +
            '<td>'+map.Day7_TotalSales+'</td>\n' +
            '<td>'+map.sale+'</td>\n' +
            '<td>'+map.ACOS+'%</td>\n' +
            '<td>'+map.tacos+'</td>\n' +
            '<td>'+map.asin_session+'</td>\n' +
            '<td>'+map.amz_product_review_num+'</td>\n' +
            '<td>'+map.amz_product_star+'</td>\n' +
            '</tr>';
        xasix.push(map.date);
        if (map.campaignTotal==0){
            yasix1.push('');
        }else {
            yasix1.push(map.campaignTotal);
        }
        if (map.ACOS==0){
            yasix2.push('');
        }else {
            yasix2.push(map.ACOS);
        }
        if (map.spend==0){
            yasix3.push('');
        }else {
            yasix3.push(map.spend);
        }
        if (map.sale==0){
            yasix4.push('');
        }else {
            yasix4.push(map.sale);
        }
        if (map.tacos=='0%'){
            yasix5.push('');
        }else if (!map.tacos){
            yasix5.push('');
        }else {
            yasix5.push(map.tacos.replace('%',''));
        }
        $("#table_1").append(html);
    }
    view2(xasix,yasix1,yasix2,yasix3,yasix4,yasix5);
}

function ranking3(startDate,endDate,keyWord,asin,campaignName) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectCampaignByDate1", // 请求的后台服务的路径
            data:{
                "startTime":startDate,
                "endTime":endDate,
                "keyWord":keyWord,
                "asin":asin,
                "campaignName":campaignName,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                f2(JSON.parse(JSON.stringify(data)));
            }

        }
    )
}


function view2(xasix,yasix1,yasix2,yasix3,yasix4,yasix5) {
    var dom = document.getElementById('container_2');
    var myChart = echarts.init(dom, null, {
        renderer: 'canvas',
        useDirtyRect: false
    });
    var app = {};

    var option;

    option = {
        legend: {
            data: ['广告花费', '广告销售额', '总销售额', 'ACOS', 'TACOS'],
            bottom :10
        },
        xAxis: {
            type: 'category',
            name: '时间',
            data: xasix
        },
        yAxis: [
            {
                type: 'value',
                name: '金额',
                position: 'left'
            },
            {
                type: 'value',
                name: 'ACOS',
                position: 'right'
            }
        ],
        series: [
            {
                name: '广告花费',
                data: yasix3,
                yAxisIndex: 0,
                type: 'line'
            },
            {
                name: '广告销售额',
                data: yasix1,
                yAxisIndex: 0,
                type: 'line'
            },
            {
                name: '总销售额',
                data: yasix4,
                yAxisIndex: 0,
                type: 'line'
            },
            {
                name: 'ACOS',
                data: yasix2,
                yAxisIndex: 1,
                type: 'line'
            },
            {
                name: 'TACOS',
                data: yasix5,
                yAxisIndex: 1,
                type: 'line'
            }
        ],
        grid: {
            x:80,
            y:250
        }
    };

    if (option && typeof option === 'object') {
        myChart.setOption(option);
    }

    window.addEventListener('resize', myChart.resize);
}

function search_day(day) {
    var keyWord = sessionStorage.getItem("keywName");
    var campaignName =sessionStorage.getItem("campaignName");
    var asin = $("#u145_input").val();
    if(null == asin || '' == asin){
        alert("asin不能为空，请按照格式输入")
        return;
    }
    var s = new Date().Format("yyyy-MM-dd");
    var startTime = new Date(new Date() - 24*60*60*1000*day).Format("yyyy-MM-dd");
    var endTime = s;
    request(startTime,endTime,asin,keyWord,campaignName);
}


function request(startDate,endDate,asin,keyWord,campaignName) {
    // 通过jQuery.ajax() 发送异步请求
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/rankingByAsin", // 请求的后台服务的路径
            data:{
                "startTime":startDate,
                "endTime":endDate,
                "asin":asin,
                "keyWord":keyWord,
                "campaignName":campaignName,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                f(JSON.parse(JSON.stringify(data)));
                ranking3(startDate,endDate,keyWord,asin,campaignName);
            }

        }
    )
}

Date.prototype.Format = function (fmt) { // author: zhz
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}