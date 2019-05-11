function getBalance(){
    $.get({
        url: "/dashboard/getbal",
        dataType:"json",
        success: function (result) {
            $("#resultBal").html(result["balance"]);
            $('#resultTime').html(result["time"]);
        }
    });
}
getBalance(); //вызвали первый раз при загрузке страницы
setInterval(getBalance, 10000);