function setParam() {
    window.open("/advertisingParam");
}

function autoAd(campaignId,adgroupId,keyWord,ruleName,flag) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/autoAd", // 请求的后台服务的路径
            data:{
                "campaignId":campaignId,
                "adgroupId":adgroupId,
                "keyWord":keyWord,
                "ruleName":ruleName,
                "flag":flag,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
            }
        }
    )
}

function fa(click,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectKeyWordAndNoOrder", // 请求的后台服务的路径
            data:{
                "click":click,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fadata(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fadata(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>Search Term</th>\n' +
        // '            <th>总订单</th>\n' +
        // '            <th>点击数超过数</th>\n' +
        // '            <th>点击超过且没出单的数量</th>\n' +
        '            <th>item_name</th>\n' +
        '            <th>Campaign Name</th>\n' +
        '            <th>Ad Group Name</th>\n' +
        '            <th>click</th>\n' +
        '            <th>出单的日期</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.Customer_Search_Term+'</td>\n' +
            // '<td>'+map.torders+'</td>\n' +
            // '<td>'+map.tsl+'</td>\n' +
            // '<td>'+map.sl+'</td>\n' +
            '<td>'+map.item_name+'</td>\n' +
            '<td>'+map.Campaign_Name+'</td>\n' +
            '<td>'+map.Ad_Group_Name+'</td>\n' +
            '<td>'+map.Clicks+'</td>\n' +
            '<td>'+map.date.substr(0,10)+'</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}




function fb(impression,ctr,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectImpressionAndCtrAndNoOrder", // 请求的后台服务的路径
            data:{
                "impression":impression,
                "ctr":ctr,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fbdata(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fbdata(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>Search Term</th>\n' +
        '            <th>item_name</th>\n' +
        '            <th>总曝光</th>\n' +
        '            <th>总点击</th>\n' +
        '            <th>SCTR</th>\n' +
        '            <th>总订单</th>\n' +
        '            <th>Campaign Name</th>\n' +
        '            <th>Ad Group Name</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.Customer_Search_Term+'</td>\n' +
            '<td>'+map.item_name+'</td>\n' +
            '<td>'+map.sumimp+'</td>\n' +
            '<td>'+map.sumclicks+'</td>\n' +
            '<td>'+map.SCTR+'</td>\n' +
            '<td>'+map.sorders+'</td>\n' +
            '<td>'+map.Campaign_Name+'</td>\n' +
            '<td>'+map.Ad_Group_Name+'</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}



function fc(click,ctr,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectClickAndCtrAndNoOrder", // 请求的后台服务的路径
            data:{
                "click":click,
                "ctr":ctr,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fcdata(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fcdata(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>Search Term</th>\n' +
        '            <th>item_name</th>\n' +
        // '            <th>总订单</th>\n' +
        // '            <th>点击数超过数</th>\n' +
        // '            <th>点击超过且没出单的数量</th>\n' +
        '            <th>CTR</th>\n' +
        '            <th>Campaign Name</th>\n' +
        '            <th>Ad Group Name</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.Customer_Search_Term+'</td>\n' +
            '<td>'+map.item_name+'</td>\n' +
            // '<td>'+map.torders+'</td>\n' +
            // '<td>'+map.tsl+'</td>\n' +
            // '<td>'+map.sl+'</td>\n' +
            '<td>'+map.ctr+'</td>\n' +
            '<td>'+map.Campaign_Name+'</td>\n' +
            '<td>'+map.Ad_Group_Name+'</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}



function fd(acos,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectSearchTermByAcos", // 请求的后台服务的路径
            data:{
                "acos":acos,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fddata(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fddata(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>Search Term</th>\n' +
        '            <th>item_name</th>\n' +
        // '            <th>Impressions</th>\n' +
        // '            <th>Clicks</th>\n' +
        // '            <th>CTR</th>\n' +
        // '            <th>CPC</th>\n' +
        // '            <th>Spend</th>\n' +
        '            <th>ACOS</th>\n' +
        '            <th>Campaign Name</th>\n' +
        '            <th>Ad Group Name</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.Customer_Search_Term+'</td>\n' +
            '<td>'+map.item_name+'</td>\n' +
            // '<td>'+map.Impressions+'</td>\n' +
            // '<td>'+map.Clicks+'</td>\n' +
            // '<td>'+map.CTR+'</td>\n' +
            // '<td>'+map.CPC+'</td>\n' +
            // '<td>'+map.Spend+'</td>\n' +
            '<td>'+map.tacos+'</td>\n' +
            '<td>'+map.Campaign_Name+'</td>\n' +
            '<td>'+map.Ad_Group_Name+'</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}



function fe(ctr,click,conversionRate,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectSearchTermByCtrAndClick", // 请求的后台服务的路径
            data:{
                "ctr":ctr,
                "click":click,
                "conversionRate":conversionRate,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fedata(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fedata(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>Search Term</th>\n' +
        '            <th>item_Name</th>\n' +
        '            <th>CTR</th>\n' +
        '            <th>Clicks</th>\n' +
        '            <th>转换率</th>\n' +
        '            <th>Campaign Name</th>\n' +
        '            <th>Ad Group Name</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.Customer_Search_Term+'</td>\n' +
            '<td>'+map.item_name+'</td>\n' +
            '<td>'+map.ctr+'</td>\n' +
            '<td>'+map.clicks+'</td>\n' +
            '<td>'+map.conversionRate+'</td>\n' +
            '<td>'+map.Campaign_Name+'</td>\n' +
            '<td>'+map.Ad_Group_Name+'</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}



function ff(click,conversionRate,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectSearchTermByClickAndConrate", // 请求的后台服务的路径
            data:{
                "click":click,
                "conversionRate":conversionRate,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                ffdata(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function ffdata(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>Search Term</th>\n' +
        '            <th>item_Name</th>\n' +
        // '            <th>符合条件的总订单</th>\n' +
        // '            <th>点击数超过数</th>\n' +
        // '            <th>点击超过且没出单的数量</th>\n' +
        '            <th>总订单</th>\n' +
        '            <th>转化率</th>\n' +
        '            <th>Campaign Name</th>\n' +
        '            <th>Ad Group Name</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.Customer_Search_Term+'</td>\n' +
            '<td>'+map.item_name+'</td>\n' +
            // '<td>'+map.sorders+'</td>\n' +
            // '<td>'+map.tsl+'</td>\n' +
            // '<td>'+map.sl+'</td>\n' +
            '<td>'+map.torders+'</td>\n' +
            '<td>'+map.conversionRate+'</td>\n' +
            '<td>'+map.Campaign_Name+'</td>\n' +
            '<td>'+map.Ad_Group_Name+'</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}


function fg(click,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectAsinByClick", // 请求的后台服务的路径
            data:{
                "click":click,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fgdata(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fgdata(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>Item_Name</th>\n' +
        '            <th>Advertised_SKU</th>\n' +
        '            <th>Advertised_ASIN</th>\n' +
        '            <th>总点击</th>\n' +
        '            <th>总订单</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.item_name+'</td>\n' +
            '<td>'+map.Advertised_SKU+'</td>\n' +
            '<td>'+map.Advertised_ASIN+'</td>\n' +
            '<td>'+map.sclicks+'</td>\n' +
            '<td>'+map.sorders+'</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}

function fh1(percent,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectStopAdvice1", // 请求的后台服务的路径
            data:{
                "percent":percent,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fh1data(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fh1data(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>item_name</th>\n' +
        '            <th>SKU</th>\n' +
        '            <th>ASIN</th>\n' +
        '            <th>Impressions</th>\n' +
        '            <th>clicks</th>\n' +
        '            <th>Day7_AdvertisedSKUSales</th>\n' +
        '            <th>Day7_AdvertisedSKU_Units</th>\n' +
        '            <th>spend</th>\n' +
        '            <th>Day7_Total_Sales</th>\n' +
        '            <th>ACOS</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.item_name+'</td>\n' +
            '<td>'+map.Advertised_SKU+'</td>\n' +
            '<td>'+map.Advertised_ASIN+'</td>\n' +
            '<td>'+map.Impressions+'</td>\n' +
            '<td>'+map.clicks+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKUSales+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKU_Units+'</td>\n' +
            '<td>'+map.spend+'</td>\n' +
            '<td>'+map.Day7_Total_Sales+'</td>\n' +
            '<td>'+map.acos+'%</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}

function fh2(percent,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectStopAdvice2", // 请求的后台服务的路径
            data:{
                "percent":percent,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fh2data(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fh2data(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>item_name</th>\n' +
        '            <th>SKU</th>\n' +
        '            <th>ASIN</th>\n' +
        '            <th>Impressions</th>\n' +
        '            <th>clicks</th>\n' +
        '            <th>Day7_AdvertisedSKUSales</th>\n' +
        '            <th>Day7_AdvertisedSKU_Units</th>\n' +
        '            <th>spend</th>\n' +
        '            <th>Day7_Total_Sales</th>\n' +
        '            <th>ACOS</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.item_name+'</td>\n' +
            '<td>'+map.Advertised_SKU+'</td>\n' +
            '<td>'+map.Advertised_ASIN+'</td>\n' +
            '<td>'+map.Impressions+'</td>\n' +
            '<td>'+map.clicks+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKUSales+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKU_Units+'</td>\n' +
            '<td>'+map.spend+'</td>\n' +
            '<td>'+map.Day7_Total_Sales+'</td>\n' +
            '<td>'+map.acos+'%</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}

function fh3(percent,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectStopAdvice3", // 请求的后台服务的路径
            data:{
                "percent":percent,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fh3data(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fh3data(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>item_name</th>\n' +
        '            <th>SKU</th>\n' +
        '            <th>ASIN</th>\n' +
        '            <th>Impressions</th>\n' +
        '            <th>clicks</th>\n' +
        '            <th>Day7_AdvertisedSKUSales</th>\n' +
        '            <th>Day7_AdvertisedSKU_Units</th>\n' +
        '            <th>spend</th>\n' +
        '            <th>Day7_Total_Sales</th>\n' +
        '            <th>ACOS</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.item_name+'</td>\n' +
            '<td>'+map.Advertised_SKU+'</td>\n' +
            '<td>'+map.Advertised_ASIN+'</td>\n' +
            '<td>'+map.Impressions+'</td>\n' +
            '<td>'+map.clicks+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKUSales+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKU_Units+'</td>\n' +
            '<td>'+map.spend+'</td>\n' +
            '<td>'+map.Day7_Total_Sales+'</td>\n' +
            '<td>'+map.acos+'%</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}

function fh4(percent,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectStopAdvice4", // 请求的后台服务的路径
            data:{
                "percent":percent,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fh4data(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fh4data(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>item_name</th>\n' +
        '            <th>SKU</th>\n' +
        '            <th>ASIN</th>\n' +
        '            <th>Impressions</th>\n' +
        '            <th>clicks</th>\n' +
        '            <th>Day7_AdvertisedSKUSales</th>\n' +
        '            <th>Day7_AdvertisedSKU_Units</th>\n' +
        '            <th>spend</th>\n' +
        '            <th>Day7_Total_Sales</th>\n' +
        '            <th>ACOS</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.item_name+'</td>\n' +
            '<td>'+map.Advertised_SKU+'</td>\n' +
            '<td>'+map.Advertised_ASIN+'</td>\n' +
            '<td>'+map.Impressions+'</td>\n' +
            '<td>'+map.clicks+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKUSales+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKU_Units+'</td>\n' +
            '<td>'+map.spend+'</td>\n' +
            '<td>'+map.Day7_Total_Sales+'</td>\n' +
            '<td>'+map.acos+'%</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}

function fh5(percent,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectStopAdvice5", // 请求的后台服务的路径
            data:{
                "percent":percent,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fh5data(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fh5data(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>item_name</th>\n' +
        '            <th>SKU</th>\n' +
        '            <th>ASIN</th>\n' +
        '            <th>Impressions</th>\n' +
        '            <th>clicks</th>\n' +
        '            <th>Day7_AdvertisedSKUSales</th>\n' +
        '            <th>Day7_AdvertisedSKU_Units</th>\n' +
        '            <th>spend</th>\n' +
        '            <th>Day7_Total_Sales</th>\n' +
        '            <th>ACOS</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.item_name+'</td>\n' +
            '<td>'+map.Advertised_SKU+'</td>\n' +
            '<td>'+map.Advertised_ASIN+'</td>\n' +
            '<td>'+map.Impressions+'</td>\n' +
            '<td>'+map.clicks+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKUSales+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKU_Units+'</td>\n' +
            '<td>'+map.spend+'</td>\n' +
            '<td>'+map.Day7_Total_Sales+'</td>\n' +
            '<td>'+map.acos+'%</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}

function fh6(percent,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectStopAdvice6", // 请求的后台服务的路径
            data:{
                "percent":percent,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fh6data(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fh6data(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>item_name</th>\n' +
        '            <th>SKU</th>\n' +
        '            <th>ASIN</th>\n' +
        '            <th>Impressions</th>\n' +
        '            <th>clicks</th>\n' +
        '            <th>Day7_AdvertisedSKUSales</th>\n' +
        '            <th>Day7_AdvertisedSKU_Units</th>\n' +
        '            <th>spend</th>\n' +
        '            <th>Day7_Total_Sales</th>\n' +
        '            <th>ACOS</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.item_name+'</td>\n' +
            '<td>'+map.Advertised_SKU+'</td>\n' +
            '<td>'+map.Advertised_ASIN+'</td>\n' +
            '<td>'+map.Impressions+'</td>\n' +
            '<td>'+map.clicks+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKUSales+'</td>\n' +
            '<td>'+map.Day7_AdvertisedSKU_Units+'</td>\n' +
            '<td>'+map.spend+'</td>\n' +
            '<td>'+map.Day7_Total_Sales+'</td>\n' +
            '<td>'+map.acos+'%</td>\n' +
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}

function fj(acos,id) {
    $.ajax(
        {
            type:"POST",// 请求的方式 GET  POST
            url:"/selectSearchTermByLowAcos", // 请求的后台服务的路径
            data:{
                "acos":acos,
            },// 提交的参数
            success:function(data){ // 响应成功执行的函数
                console.log(data);
                fjdata(JSON.parse(JSON.stringify(data)),id);
            }
        }
    )
}

function fjdata(data,id) {
    $('#'+id+'').html("");
    $('#'+id+'').append('<tr>\n' +
        '            <th>ACOS</th>\n' +
        '            <th>Search Term</th>\n' +
        '            <th>Campaign Name</th>\n' +
        '            <th>AdGroup Name</th>\n' +
        '          </tr>');
    for (var i in data){
        var html = "";
        var map = data[i];
        html+='<tr>\n' +
            '<td>'+map.acos+'%</td>\n' +
            '<td>'+map.Customer_Search_Term+'</td>\n' +
            '<td>'+map.Campaign_Name+'</td>\n' +
            '<td>'+map.Ad_Group_Name+'</td>\n' +
            // '<td><a   href="javascript:void(0);"  onclick="autoAd(\''+map.campaign_id+'\'' +
            // ',\''+map.ad_group_id+'\',\''+map.Campaign_Name+'\',\'过去30天,被暂停的组里面有ACOS 很低的 Search Term, 就提醒。ACOS\',\'1\')";>反关键字</a></td>\n'+
            // '<td><a   href="javascript:void(0);"  onclick="autoAd(\''+map.campaign_id+'\'' +
            // ',\''+map.ad_group_id+'\',\''+map.Campaign_Name+'\',\'过去30天,被暂停的组里面有ACOS 很低的 Search Term, 就提醒。ACOS\',\'2\')";>反Asin</a></td>\n'+
            '</tr>';
        $('#'+id+'').append(html);
        $('#'+id+'').css("display","block");
    }
}


