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

/*function modifyAFoodRegistration(date){
    var amount = document.getElementById("edit-amount-input");
    var btnModify = document.getElementsByName("save-modification")[0];
    var registrationId = btnModify.id;
    alert(registrationId)
    var xhr = new XMLHttpRequest();
    var url = "/food/registration/".concat(registrationId.toString()).concat("?registrationDate=");

    xhr.onreadystatechange = function() { // listen for state changes
        if (xhr.readyState == 4 && xhr.status == 200) { // when completed we can move away
            window.location = url.concat(date);
        }
    }
    xhr.open("PUT", url.concat(date), true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        amount: amount.value
    }))
    return
}*/

function openEditModal(name, registrationId, date){
    //clone dialog and remove ids to ensure uniqueness
    //var $modal = $('#ModificationModal*').clone().removeAttr('id');
    var $originalModal = $('#ModificationModal')
    var $modal = $originalModal.clone().removeAttr('id');


    $modal.find('[name="modify-text"]').text(name);
    var $btnModify = $modal.find('[name="save-modification"]');

    var handler = function (){
        var $amount = $modal.find('[name="edit-amount-input"]');
        var xhr = new XMLHttpRequest();
        var urlForPut = "/food/registration/".concat(registrationId).concat("/?registrationDate=").concat(date);
        var urlForGet = "/food/registration/".concat("?registrationDate=").concat(date);

        xhr.onreadystatechange = function() { // listen for state changes
            if (xhr.readyState == 4 && xhr.status == 200) { // when completed we can move away
                window.location = urlForGet;
            }
        }
        xhr.open("PUT", urlForPut, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSON.stringify({
            amount: $amount.val()
        }))
        return
    };

    $btnModify.click(handler);

    //show dialo
    $modal.modal('show');
}