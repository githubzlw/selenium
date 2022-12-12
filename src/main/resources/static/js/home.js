$(document).ready(function(){
    var keyWord = sessionStorage.getItem("homeKeyword");
    if(null == keyWord || '' == keyWord){
    }else {
        $("#u6_input").val(keyWord);
        $("#u41_input").val('30');
        search();
        document.getElementById('u4').click();
        $("#u12_input").val(sessionStorage.getItem("homeAsin"));
    }

});

function search() {
    var keyWord = $("#u6_input").val();
    // 获取输入框中的内容
    if(null == keyWord || '' == keyWord){
        alert("关键词不能为空，请按照格式输入")
        return;
    }
    var searchDate = $("#u1_div").val();
    if(null == searchDate || '' == searchDate){
        alert("日期不能为空，请按照格式输入")
        return;
    }
    var rankMethod = $('select#u39_input option:selected').val();
    var num = $("#u41_input").val();
    if(null == num || '' == num){
        alert("数量不能为空，请按照格式输入")
        return;
    }
    $("#check_all").attr('checked', false);
    // 通过jQuery.ajax() 发送异步请求
        $.ajax(
            {
                type:"POST",// 请求的方式 GET  POST
                url:"/search", // 请求的后台服务的路径
                data:{
                    "searchDate":searchDate,
                    "keyWord":keyWord,
                    "rankMethod":rankMethod,
                    "num":num,
                },// 提交的参数
                success:function(res){ // 响应成功执行的函数
                    console.log(res);
                    $("#u10_text").text(keyWord);
                    if(res!=null && res!="" ){
                        $("#u15").html("");
                        for (var j in res) {
                            var alertHtml = "";
                            var data = res[j];
                            alertHtml += '<div class="box">';
                            alertHtml += '<a class="a-link-normal" href="'+data.amzUrl+'" target="_blank">';
                            alertHtml += '<div class="list_img">';
                            alertHtml += '<img ';
                            alertHtml += 'class="s-image lazy" src="';
                            // if (j>7){
                            //     alertHtml += 'images/home/lazy.gif" data-original="';
                            // }
                            alertHtml += data.amzProductImg+'"';
                            alertHtml += '></div>';
                            alertHtml += '<div class="list_detail">';
                            if (data.amzProductStyle==1){
                                alertHtml += '<div class="spon">Sponsored<img src="/images/home/spon.png"></div>';
                            }
                            alertHtml += '<div class="list_title">'+data.amzProductName+'</div>';
                            alertHtml += '<div class="list_review">';
                            alertHtml += '<span class="list_stars">'+data.amzProductStar+'</span>';
                            alertHtml += '<span class="list_num">&ensp;'+data.amzProductReviewNum+'</span></div>';
                            alertHtml += '<div class="list_price"><span class="true"><span class="a-price-whole">'+data.amzProductPrice+'</span></div></div></a>';
                            alertHtml += '<label class="list_delete"><input type="checkbox" name="isToBeGiftWrapped" value="'+data.id+'"></label>';
                            // alertHtml += '<label class="list_delete"><input type="checkbox" name="isToBeGiftWrapped" value="'+data.amzAsin+'"></label>';
                            $("#u15").append(alertHtml);
                        }
                    }
                }
            }
        )
}


function competeProduct() {
    var keyWord = $("#u6_input").val();
    console.log(keyWord)
    var asin = $("#u12_input").val();
    var ids = getGoodId()
    // 获取输入框中的内容
    if(null == keyWord || '' == keyWord){
        alert("关键词不能为空，请按照格式输入")
        return;
    }
    if(null == asin || '' == asin){
        alert("asin不能为空，请按照格式输入")
        return;
    }
    if(null == ids || '' == ids){
        alert("请勾选商品再分析")
        return;
    }
    var searchDate = $("#u1_div").val();
    // 通过jQuery.ajax() 发送异步请求
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/competeProduct", // 请求的后台服务的路径
            data:{
                "searchDate":searchDate,
                "keyWord":keyWord,
                "asin":asin,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data)
                sessionStorage.setItem("ids",ids);
                sessionStorage.setItem("amzProductImg",data.amzProductImg);
                sessionStorage.setItem("amzProductName",data.amzProductName);
                sessionStorage.setItem("amzProductReviewNum",data.amzProductReviewNum);
                sessionStorage.setItem("amzProductPrice",data.amzProductPrice);
                sessionStorage.setItem("amzAsin",data.amzAsin);
                sessionStorage.setItem("amzProductStar",data.amzProductStar);
                sessionStorage.setItem("keyWord",keyWord);
                sessionStorage.setItem("num", $("#u41_input").val());
                sessionStorage.setItem("amzUrl", data.amzUrl);
                location.href = "page_1?ids="+ids+ "&asin="+asin+ "&keyWord="+keyWord;
            }

        }
    )
    
}

//获取购物车选中商品的pid
function getGoodId(){
    var  strPid="";

    $.each($('input:checkbox:checked', '.list_delete'),function(){

        var res=$(this).attr('value');
        if(res!=="")
        {
            if(strPid=="")
            {
                strPid=res;
            }else
            {
                strPid=strPid+","+res;
            }
        }

    });
    return strPid;
}


function keySearch() {
    var e = event || window.event;
    if (e && e.keyCode == 13) { //回车键的键值为13
        search();
    }
}

function ranking() {
    var keyWord = $("#u6_input").val();
    var asin = $("#u12_input").val();
    // 获取输入框中的内容
    if(null == keyWord || '' == keyWord){
        alert("关键词不能为空，请按照格式输入")
        return;
    }
    if(null == asin || '' == asin){
        alert("asin不能为空，请按照格式输入")
        return;
    }
    var s = new Date().Format("yyyy-MM-dd");
    var startTime = new Date(new Date() - 24*60*60*1000*30).Format("yyyy-MM-dd");
    var endTime = s;
    // 通过jQuery.ajax() 发送异步请求
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/rankingByAsin", // 请求的后台服务的路径
            data:{
                "startTime":startTime,
                "endTime":endTime,
                "asin":asin,
                "keyWord":keyWord,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data)
                sessionStorage.setItem("keyWord",keyWord);
                sessionStorage.setItem("data", JSON.stringify(data));
                sessionStorage.setItem("time_start", startTime);
                sessionStorage.setItem("time_end", endTime);
                sessionStorage.setItem("asin_2", asin);
                sessionStorage.setItem("campaignName", "");
                location.href = "page_2";
            }

        }
    )
}


function c(){
    //1.获取编号前面的复选框
    var checkAllEle = document.getElementById("check_all");
    //2.对编号前面复选框的状态进行判断
    if(checkAllEle.checked==true){
        //3.获取下面所有的复选框
        var checkOnes = document.getElementsByName("isToBeGiftWrapped");
        //4.对获取的所有复选框进行遍历
        for(var i=0;i<checkOnes.length;i++){
            //5.拿到每一个复选框，并将其状态置为选中
            checkOnes[i].checked=true;
        }
    }else{
        //6.获取下面所有的复选框
        var checkOnes = document.getElementsByName("isToBeGiftWrapped");
        //7.对获取的所有复选框进行遍历
        for(var i=0;i<checkOnes.length;i++){
            //8.拿到每一个复选框，并将其状态置为未选中
            checkOnes[i].checked=false;
        }
    }
};

function advertisingViolation() {
    window.open("/advertisingViolation");
}

function campaign_list() {
    window.open("/campaign_list");
}

function ad_logs() {
    window.open("/ad_log");
}

function search_term_compare() {
    sessionStorage.setItem("searchTermAsin", "");
    window.open("/search_term_compare");
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