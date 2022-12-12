$(document).ready(function(){
    var asin = sessionStorage.getItem("searchTermAsin");
    console.log(asin)
    if(null == asin || '' == asin){

    }else {
        $("#u63_input").val(asin);
        $("#u65_input").val(new Date(new Date() - 24*60*60*1000*10).Format("yyyy-MM-dd"));
        compare();
    }

});

function compare() {
    var asin = $("#u63_input").val();
    if(null == asin || '' == asin){
        alert("asin不能为空，请按照格式输入")
        return;
    }
    var startDate = $("#u65_input").val();
    var endDate = $("#u66_input").val();
    // 通过jQuery.ajax() 发送异步请求
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/compareProductByAsinAndDate", // 请求的后台服务的路径
            data:{
                "startTime":startDate,
                "endTime":endDate,
                "asin":asin,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                f(JSON.parse(JSON.stringify(data)));
            }

        }
    )
}


function f(data) {
    console.log(data)
    $("#table").html("");
    $("#table").append('<tr>\n' +
        '            <th>Search Term Name</th>\n' +
        '            <th>广告曝光量</th>\n' +
        '            <th>广告排名</th>\n' +
        '            <th>自然排名</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.keyword+'</td>\n' +
            '<td>'+map.impressions+'</td>\n' +
            '<td>'+map.amz_product_not_normal_count+'</td>\n' +
            '<td>'+map.amz_product_normal_count+'</td>\n' +
            '</tr>';
        $("#table").append(html);
    }
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