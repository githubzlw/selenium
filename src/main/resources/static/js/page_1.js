$(document).ready(function(){
    var ids = sessionStorage.getItem("ids");
    var keyWord = sessionStorage.getItem("keyWord");
    var rankMethod = $('select#u67_input option:selected').val();
    var html="";
    if (sessionStorage.getItem("amzProductImg")!=null){
        html +='<div class="our_pro clearfix">\n' +
            '                <div class="our_img left">\n' +
            '                  <img src="'+sessionStorage.getItem("amzProductImg")+'" alt="">\n' +
            '                </div>\n' +
            '                <div class="our_det left">' +
            ' <h3><a href="'+sessionStorage.getItem("amzUrl")+'">'+sessionStorage.getItem("amzProductName")+'</a> </h3>\n' +
            '                  <div class="list_review">\n' +
            '                      <span class="list_stars">'+sessionStorage.getItem("amzProductStar")+'</span>\n' +
            '                      <span class="list_num">'+sessionStorage.getItem("amzProductReviewNum")+'</span>\n' +
            '                  </div>\n' +
            '                  <p class="our_price">'+sessionStorage.getItem("amzProductPrice")+'</p>\n' +
            '                  <p class="our_asin">ASIN: '+sessionStorage.getItem("amzAsin")+'</p>\n' +
            '                </div>\n' +
            '              </div>';
    }

    $("#u44").append(html);
    var strings = keyWord.split(";");
    for (var i = 0; i < strings.length; i++) {
        var string = strings[i];
        $("#u43").append('<button class="key_word" onclick="f(\''+string+'\')">'+string+'</button>');
    }
    f(strings[0],rankMethod);
});


function f(keyWordName,rankMethod) {
    var ids = sessionStorage.getItem("ids");
    sessionStorage.setItem("keyWordName", keyWordName);
    if (null == rankMethod || '' == rankMethod){
        var rankMethod = $('select#u67_input option:selected').val();
    }
    if (null == keyWordName || '' == keyWordName){
        var keyWordName = sessionStorage.getItem("keyWordName");
    }
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/compareCompeteProduct", // 请求的后台服务的路径
            data:{
                "rankMethod":rankMethod,
                "keyWord":keyWordName,
                "ids":ids,
            },// 提交的参数
            success:function(res){ // 响应成功执行的函数
                var count =0;
                if(res!=null && res!="" ){
                    $("#u59").html("");
                    for (var j in res) {
                        var alertHtml = "";
                        var data = res[j];
                        if (data.amzProductPrice!=null){
                            if (sessionStorage.getItem("amzProductPrice").replace("$","").replace("US","").replace(",","")-data.amzProductPrice.replace("$","").replace("US","").replace(",","")>0){
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
                                alertHtml += '<span class="list_stars"><b>'+data.amzProductStar+'</b></span>';
                                alertHtml += '<span class="list_num">&ensp;'+data.amzProductReviewNum+'</span></div>';
                                alertHtml += '<div class="list_price"><span class="true"><span class="a-price-whole">'+data.amzProductPrice+'</span></div></div></a>';
                                // alertHtml += '<label class="list_delete"><input type="checkbox" name="isToBeGiftWrapped" value="'+data.id+'"></label>';
                                $("#u59").append(alertHtml);
                                count++;
                            }
                        }
                    }
                }
                var num = sessionStorage.getItem("num");
                $("#u56_div").html("");
                // $("#u58").append('<div id="u58_div">共计'+count+'个，占当前搜索总数的&nbsp; '+((count/num) * 100).toFixed(3)+' %</div>');
                $("#u56_div").append('<p><span class="add_tit">价格低于我司商品的竞品</span> <span class="add_date">共计'+count+'个，占当前搜索总数的&nbsp; '+((count/num) * 100).toFixed(3)+' %</span></p>');
            }
        }
    )
}

function f1(){
    f(sessionStorage.getItem("keyWordName"));
}