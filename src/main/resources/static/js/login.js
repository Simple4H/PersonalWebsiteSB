$(document).ready(function () {
    $.ajax({
        type        : 'post',
        url         : 'https://www.cxm520hyq.com/user/get_user_info.do',
        contentType : 'application/json;charset=utf-8',
        dataType    : 'json',
        data        :  data
    })
});