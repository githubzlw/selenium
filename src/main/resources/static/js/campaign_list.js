function f(name,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectDataByCampaign", // 请求的后台服务的路径
            data:{
                "campaignName":name,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                selectDataByCampaign(JSON.parse(JSON.stringify(data)),id,name);
            }

        }
    )
}

function selectDataByCampaign(data,id,name) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>Search Term</th>\n' +
        '            <th>广告Impression</th>\n' +
        '            <th>广告Clicks</th>\n' +
        '            <th>广告CPC</th>\n' +
        '            <th>当月搜索量</th>\n' +
        // '            <th>ASIN</th>\n' +
        '            <th>销量</th>\n' +
        '            <th>30日ACOS/7日ACOS</th>\n' +
        '            <th>后台数据抓取日期</th>\n' +
        '            <th>自然排名</th>\n' +
        '            <th>广告排名</th>\n' +
        '            <th>爬虫数据抓取日期</th>\n' +
        '            <th>前5,15竞品数量</th>\n' +
        '          </tr>');
    var searchindex=0;
    sessionStorage.setItem("pic","0");
    for (var i in data){
        searchindex=id+''+searchindex+1;
        var strDiv1= createDivByData(data[i].before_5_product);
        var strDiv2= createDivByData(data[i].before_15_product);
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.customer_search_term+'</td>\n' +
            '<td>'+map.Impressions+'</td>\n' +
            '<td>'+map.Clicks+'</td>\n' +
            '<td>'+map.cpc+'</td>\n' ;
        if(map.searchCount){
            html+= '<td>'+map.searchCount+'</td>\n';
        }else {
            html+= '<td>-</td>\n' ;
        }
        html+=
            // '<td onclick="searchTerm(\''+map.Advertised_ASIN+'\')">'+map.Advertised_ASIN+'</td>\n' +
            '<td>'+map.Day7_TotalOrders+'</td>\n' +
            '<td>'+map.TACOS+'%,'+map.acos7+'%</td>\n' +
            '<td>'+map.s_date.substr(0,10)+'</td>\n' +
            '<td>'+map.amz_product_normal_count1+'|'+map.search_date1+'</td>\n' +
            '<td>'+map.amz_product_not_normal_count1+'|'+map.search_date2 ;
        if (map.index0_img && ''!= map.index0_img){
            html+= '<img width="70px" src="'+map.index0_img+'"/>'+map.index0_price+'';
        }
        if (map.index1_img && ''!= map.index1_img){
            html+= '<img width="70px" src="'+map.index1_img+'"/>'+map.index1_price+'';
        }
        if (map.index2_img && ''!= map.index2_img){
            html+= '<img width="70px" src="'+map.index2_img+'"/>'+map.index2_price+'';
        }
        if (map.index3_img && ''!= map.index3_img){
            html+= '<img width="70px" src="'+map.index3_img+'"/>'+map.index3_price+'</td>\n';
        }
        html+=   '<td>'+map.search_date.substr(0,10)+'</td>\n' +
            '<td>'+map.before_5_num+','+map.before_15_num+'<a   href="javascript:void(0);"  onclick="view(\'searchindex'+searchindex+'\')";>点击查看,</a><a   href="javascript:void(0);"  onclick="choose(\''+map.customer_search_term+'\',\''+map.Advertised_ASIN+'\')";>竞品选择</a></td>\n' +
            // '<td><a   href="javascript:void(0);"  onclick="view(\'searchindex'+searchindex+'\')";>点击查看</a></td>\n' +
            // '<td><a   href="javascript:void(0);"  onclick="choose(\''+map.customer_search_term+'\',\''+map.Advertised_ASIN+'\')";>竞品选择</a></td>\n' +
            '<td><a   href="javascript:void(0);"  onclick="page(\''+map.customer_search_term+'\',\''+map.Advertised_ASIN+'\',\''+name+'\')";>查看排名</a></td>\n' ;
        // if('' != map.amz_product_img && '1'!=sessionStorage.getItem("pic")){
        //     sessionStorage.setItem("pic","1");
        //     html+= '<td><img width="70px" src="'+map.amz_product_img+'"/></td>\n';
        // }

        html+=  '</tr><tr><td colspan="11">' +
            '<div style="display: none" id="'+'searchindex'+searchindex+'">'+
            '<div>前5'+strDiv1+'</div>'+
            '<div>前6-15'+strDiv2+'</div></div>'
            +'</td></tr>'

        ;
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");

    }
}

function searchTerm(asin) {
    sessionStorage.setItem("searchTermAsin", asin);
    window.open("search_term_compare");
}

function createDivByData(ob){
    if  (ob == '-'){
        return "";
    }

    var html = "";
    for (var i in ob) {
        var map = ob[i];
        html+='<img width="70px" src="'+map.amz_product_img+'"/>'+
               map.amz_product_price
            ;
    }
    return html;
}



function view(searchindex) {
    $('#'+searchindex+'').css("display","block");
}


function page(keyWord,asin,name) {
    var s = new Date().Format("yyyy-MM-dd");
    var startTime = new Date(new Date() - 24*60*60*1000*30).Format("yyyy-MM-dd");
    var endTime = s;
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/rankingByAsin", // 请求的后台服务的路径
            data:{
                "startTime":startTime,
                "endTime":endTime,
                "asin":asin,
                "keyWord":keyWord,
                "campaignName":name,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data)
                sessionStorage.setItem("keyWord",keyWord);
                sessionStorage.setItem("data", JSON.stringify(data));
                sessionStorage.setItem("time_start", startTime);
                sessionStorage.setItem("time_end", endTime);
                sessionStorage.setItem("asin_2", asin);
                sessionStorage.setItem("campaignName", name);
                window.open("page_2");
            }

        }
    )
}

function search() {
    var Campaign = $('.Campaign').val();
    var Portfolio = $('.Portfolio').val();
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectCampaignByTimeAndPerson", // 请求的后台服务的路径
            data:{
                "campaignName":Campaign,
                "portfolio":Portfolio,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data)
                $('#campaignList').css("display","none");
                $('#campaignList1').html("");
                for (var i in data){
                    var html = "";
                    var map = data[i];
                    html+='<ul>\n' +
                        '<span onclick="f(\''+map.Campaign_Name+'\',\''+i+'list\')" style="cursor: pointer; color:deepskyblue">'+map.Campaign_Name+'</span>\n' +
                        '<span  style=" color:deepskyblue">昨日花费：'+map.spend3+'</span>\n' +
                        '<span  style=" color:deepskyblue">过去七日花费：'+map.spend2+'</span>\n' +
                        '<span  style=" color:deepskyblue">过去30天花费：'+map.spend1+'</span>\n' +
                        '<table border="1px" cellspacing="0px" width="1500" id="'+i+'list" class="table" style="display: none;border-style:hidden;">\n' +
                        '\n' +
                        '</table>\n' +
                        '</ul>'
                    $('#campaignList1').append(html);
                }
            }

        }
    )
}

function choose(keyword,asin) {
    sessionStorage.setItem("homeKeyword", keyword);
    sessionStorage.setItem("homeAsin", asin);
    window.open("index");
}

function asin(name,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectDataByAsin", // 请求的后台服务的路径
            data:{
                "asin":name,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                showAsin(JSON.parse(JSON.stringify(data)),id);
                keyWordList(name);
            }
        }
    )
}


function showAsin(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>Search Term</th>\n' +
        '            <th>Campaign Name</th>\n' +
        '            <th>过去2天自然排名</th>\n' +
        '            <th>过去2天广告排名</th>\n' +
        '            <th>过去7天自然排名</th>\n' +
        '            <th>过去7天广告排名</th>\n' +
        '            <th>过去14天自然排名</th>\n' +
        '            <th>过去14天广告排名</th>\n' +
        '            <th>过去21天自然排名</th>\n' +
        '            <th>过去21天广告排名</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.Customer_Search_Term+'</td>\n' +
            '<td>'+map.Campaign_Name+'</td>\n' +
            '<td>'+map.twoNormalCount+'</td>\n' +
            '<td>'+map.twoNotNormalCount+'</td>\n' +
            '<td>'+map.sevenNormalCount+'</td>\n' +
            '<td>'+map.sevenNotNormalCount+'</td>\n' +
            '<td>'+map.fourteenNormalCount+'</td>\n' +
            '<td>'+map.fourteenNotNormalCount+'</td>\n' +
            '<td>'+map.tweoNormalCount+'</td>\n' +
            '<td>'+map.tweoNotNormalCount+'</td>\n' +
            '</tr>'

        ;
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");

    }
}


function keyWordList(asin) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectAcosByAsin", // 请求的后台服务的路径
            data:{
                "asin":asin,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                showAcos(JSON.parse(JSON.stringify(data)),'list'+asin);
            }

        }
    )
}

function showAcos(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>Search Term</th>\n' +
        '            <th>过去7天acos</th>\n' +
        '            <th>过去30天acos</th>\n' +
        '            <th>是否是不活跃</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.Customer_Search_Term+'</td>\n' +
            '<td>'+map.acos_7+'%</td>\n' +
            '<td>'+map.acos_30+'%</td>\n' +
            '<td>'+map.Impressions+'</td>\n' +
            '</tr>'
        ;
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}

function change() {
    if ($('#text1').css("display")=="none"){
        $('#text1').css("display","block");
        $('#text2').css("display","block");
        $('#search').css("display","block");
        $('#campaignList').css("display","block");
        $('#campaignList1').css("display","block");
        $('#asinList').css("display","none");
    }else{
        $('#text1').css("display","none");
        $('#text2').css("display","none");
        $('#search').css("display","none");
        $('#campaignList').css("display","none");
        $('#campaignList1').css("display","none");
        $('#asinList').css("display","block");
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