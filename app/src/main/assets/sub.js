function loadSub(reload){
    $.get("/api/sub/all",function(data, status){
        window.list = JSON.parse(data);
        var html = "";
        for (var i = 0; i < list.length; i++) {
            let error = list[i].errorCount <= 0 ? '' : `失败次数：${list[i].errorCount}，`;
            html += `<li class="sub_item"><div><a href="javascript:;">${list[i].title}</a></div><div>源数：${list[i].rulesCount}，${error}更新时间：${list[i].modifyDate}</div></li>`;
        }
        $(".sub-list").html(html);
        $(".sub_item").click(function(){
            for (var i = 0; i < window.list.length; i++) {
                if($(this).find("a").text() == window.list[i].title){
                    $("#title").val(window.list[i].title);
                    $("#url").val(window.list[i].url);
                    return;
                }
            }
        });
        if(reload) {
            setTimeout(() => {
                loadSub(false);
            }, 1500);
        }
     });
}
window.loadSub = loadSub;

$("#clear").click(function(){
    $("#title").val("");
    $("#url").val("");
});
$("#back").click(function(){
    window.location.href = '/';
});

$("#info-modal-btn").click(function(){
    $("#modal-info").modal('hide');
});
function showInfo(text){
    $("#modal-info-content").text(text);
    $("#modal-info").modal('show');
}
$("#save").click(function(){
    let t = $("#title").val();
    let u = $("#url").val();
    if(t == null || t == ""){
        showInfo('标题不能为空');
        return;
    }
    if(u == null || u == "" || u.indexOf("http") != 0){
        showInfo('订阅地址有误');
        return;
    }
    u = encodeURI(u);
    t = encodeURI(t);
    $.get(`/api/sub/save?title=${t}&url=${u}&use=true`,function(data,status){
        loadSub(true);
        $("#clear").click();
        showInfo('订阅已保存');
    });
});
$("#del").click(function(){
    let t = $("#title").val();
    if(t == null || t == ""){
        showInfo('标题不能为空');
        return;
    }
    $.get(`/api/sub/del?title=${t}`,function(data,status){
        loadSub();
        $("#clear").click();
        showInfo('订阅已删除');
    });
});
function copy(input){
    let text = document.createElement('textarea');
    document.body.appendChild(text);
    text.value = input;
    text.select();
    document.execCommand('Copy');
    document.body.removeChild(text);
}
$("#batchShare").click(function(){
    let config = { data:[], version: 0};
    for (var i = 0; i < window.list.length; i++) {
        let t = window.list[i].title;
        let u = window.list[i].url;
        config.data.push({
            name: t,
            url: u
        });
    }
    copy(JSON.stringify(config));
    showInfo("已复制到剪贴板");
});
var sbTimer;
function showBatchErr(){
    $("#sb-btn").popover('show');
    if(sbTimer != null){
        clearTimeout(sbTimer);
    }
    sbTimer = setTimeout(() => {
        $("#sb-btn").popover('hide');
    }, 3000);
}
$("#sb-btn").click(function(){
    let data = [];
    let t = $("#sb-textarea").val().trim();
    if(t.length > 1 && t[0] == "{" && t[t.length - 1] == "}"){
        let d = JSON.parse(t);
        data = d.data;
        console.log("sb-data", data);
        if(data == null || data.length < 1){
            showBatchErr();
            return
        }
    } else {
        let arr = t.split("\n");
        console.log(arr.length);
        if(arr.length < 1 || t.length < 1){
            showBatchErr();
            return
        }
        data = [];
        for(let i = 0; i < arr.length; i++){
            let it = arr[i].trim();
            let its = it.split(",");
            if(its.length != 2){
                continue;
            }
            data.push({
                name: its[0].trim(),
                url: its[1].trim()
            })
        }
    }
    if(data.length > 0){
        let count = 0;
        for(let i = 0; i < data.length; i++){
            let u = encodeURI(data[i].url);
            let t = encodeURI(data[i].name);
            $.get(`/api/sub/save?title=${t}&url=${u}&use=true`,function(data,status){
                console.log(data, status)
            });
            count++;
        }
        loadSub(true);
        showInfo('已保存' + count + "个订阅源");
    }
    $("#sb-btn").popover('hide');
    $("#batchSave").click();
});

$("#sb-del-btn").click(function(){
    for (var i = 0; i < window.list.length; i++) {
        let t = window.list[i].title;
        $.get(`/api/sub/del?title=${t}`,function(data,status){

        });
    }
    loadSub(true);
    $("#modal-batch-del").click();
});

window.loadSub(false);