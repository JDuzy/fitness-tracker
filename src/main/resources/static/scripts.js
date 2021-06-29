function registerFood(){
    var foodAnchor = document.getElementsByClassName("list-group-item active")[0];
    var amount = document.getElementById("amount-input");
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/food/registration", true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        foodId: foodAnchor.id,
        amount: amount.value
    }))
    return
}
document.get