
function f(data) {
    console.log(data)
    $("#table").html("");
    $("#table").append('<tr>\n' +
        '            <th>日期</th>\n' +
        '            <th>广告花费</th>\n' +
        '            <th>广告销售额</th>\n' +
        '            <th>广告总销售额</th>\n' +
        '            <th>TACOS</th>\n' +
        '            <th>Review 数量</th>\n' +
        '            <th>星级</th>\n' +
        '          </tr>');
    var xasix = [];
    var yasix1 = [];
    var yasix2 = [];
    var yasix3 = [];
    var yasix4 = [];
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.date+'</td>\n' +
            '<td>'+map.spend+'</td>\n' +
            '<td>'+map.campaignTotal+'</td>\n' +
            '<td>'+map.Day7_TotalSales+'</td>\n' +
            '<td>'+map.TACOS+'</td>\n' +
            '<td>'+map.amz_product_review_num+'</td>\n' +
            '<td>'+map.amz_product_star+'</td>\n' +
            '</tr>';
        xasix.push(map.date);
        yasix1.push(map.campaignTotal);
        yasix2.push(map.TACOS);
        yasix3.push(map.spend);
        yasix4.push(map.Day7_TotalSales);
        $("#table").append(html);
    }
    view(xasix,yasix1,yasix2,yasix3,yasix4);
}

function ranking() {
    var campaignName = $("#u145_input").val();
    if(null == campaignName || '' == campaignName){
        alert("campaignName不能为空，请按照格式输入")
        return;
    }
    var startDate = $("#u70_div").val();
    var endDate = $("#u72_div").val();
    // 通过jQuery.ajax() 发送异步请求
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectCampaignByDate", // 请求的后台服务的路径
            data:{
                "campaignName":campaignName,
                "startTime":startDate,
                "endTime":endDate,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                f(JSON.parse(JSON.stringify(data)));
            }

        }
    )
}

function keySearch() {
    var e = event || window.event;
    if (e && e.keyCode == 13) { //回车键的键值为13
        ranking();
    }
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
            data: ['广告花费', '广告销售额', '总销售额', 'TACOS'],
            bottom :10
        },
        xAxis: {
            type: 'category',
            name: '时间',
            data: xasix
            //data: ['05-07','06-05','06-10','06-15','06-20','06-25','06-26','06-27']
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
                //data: [0,1,5,2,5,8,6],
                yAxisIndex: 0,
                type: 'line'
            },
            {
                name: '广告销售额',
                data: yasix1,
                //data: [5,1,8,3,4,6,1],
                yAxisIndex: 0,
                type: 'line'
            },
            {
                name: '总销售额',
                data: yasix4,
                //data: [58,1,35,99,88,102,55],
                yAxisIndex: 0,
                type: 'line'
            },
            {
                name: 'TACOS',
                data: yasix2,
                //data: [0.3,0.5,1.4,5.5,8.1,3.5,2.6],
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
