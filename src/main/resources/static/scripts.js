function registerFood(date){
    var foodAnchor = document.getElementsByClassName("list-group-item active")[0];
    var amount = document.getElementById("amount-input");
    var xhr = new XMLHttpRequest();
    var url = "/food/registration?registrationDate="
    xhr.onreadystatechange = function() { // listen for state changes
        if (xhr.readyState == 4 && xhr.status == 200) { // when completed we can move away
            window.location = url.concat(date);
        }
    }
    xhr.open("POST", url.concat(date), true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        foodId: foodAnchor.id,
        amount: amount.value
    }))
    return
}

function modifyAFoodRegistration(date){
    var foodAnchor = document.getElementsByClassName("list-group-item active")[0];
    var amount = document.getElementById("amount-input");
    var xhr = new XMLHttpRequest();
    var url = "/food/registration?registrationDate="
    xhr.onreadystatechange = function() { // listen for state changes
        if (xhr.readyState == 4 && xhr.status == 200) { // when completed we can move away
            window.location = url.concat(date);
        }
    }
    xhr.open("POST", url.concat(date), true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        foodId: foodAnchor.id,
        amount: amount.value
    }))
    return
}

function openEditModal(name, registrationId){
    //clone dialog and remove ids to ensure uniqueness
    var $modal = $('#ModificationModal').clone().removeAttr('id');

    $modal.find("#modify-text").text(name);
    $modal.find("#save-modification").attr('id', registrationId);

    //show dialo
    $modal.modal('show');
}