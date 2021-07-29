//Person
function updatePerson(){
    var sexInput = document.getElementById("sexInput")
    var dateInput = document.getElementById("dateInput")
    var heightInput = document.getElementById("heightInput")
    var weightInput = document.getElementById("weightInput")
    var objective = document.getElementById("objectiveInput")
    var physicalActivity = document.getElementById("physicalActivityInput")

    var xhr = new XMLHttpRequest();
    var url = "/person/update"
    setXhrRedirect(xhr, "/food/registration")
    xhr.open("PUT", url, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        sex: sexInput.value,
        dateOfBirth: dateInput.value,
        height: heightInput.value,
        weight: weightInput.value,
        objective: objective.value,
        physicalActivity: physicalActivity.value
    }))
}

//FoodRegistration
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
}

function setXhrRedirect(xhr, urlForGet){
    xhr.onreadystatechange = function() { // listen for state changes
        if (xhr.readyState == 4 && xhr.status == 200) { // when completed we can move away
            window.location = urlForGet;
        }
    }
    return xhr;
}

function openFoodRegistrationEditModal(name, registrationId, date){
    //clone dialog and remove ids to ensure uniqueness
    //var $modal = $('#ModificationModal*').clone().removeAttr('id');
    var $originalModal = $('#ModificationModal')
    var $modal = $originalModal.clone().removeAttr('id');


    $modal.find('[name="modify-update-text"]').text(name);
    var $btnModify = $modal.find('[name="save-modification"]');

    var handler = function (){
        var $amount = $modal.find('[name="edit-amount-input"]');
        var xhr = new XMLHttpRequest();
        var urlForPut = "/food/registration/".concat(registrationId);
        var urlForGet = "/food/registration/".concat("?registrationDate=").concat(date);
        setXhrRedirect(xhr, urlForGet);

        xhr.open("PUT", urlForPut, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSON.stringify({
            amount: $amount.val()
        }))
    };

    $btnModify.click(handler);

    //show dialo
    $modal.modal('show');
}

function openFoodRegistrationDeleteModal(name, registrationId, date){
    var $originalModal = $('#EliminationModal')
    var $modal = $originalModal.clone().removeAttr('id');
    $modal.find('[name="modify-delete-text"]').text("Are you sure you want to delete the registration of ".concat(name).concat("?"));
    var $btnDelete = $modal.find('[name="delete"]');

    var handler = function (){
        var xhr = new XMLHttpRequest();
        var urlForDelete = "/food/registration/".concat(registrationId);
        var urlForGet = "/food/registration/".concat("?registrationDate=").concat(date);

        setXhrRedirect(xhr, urlForGet);

        xhr.open("DELETE", urlForDelete, true);
        xhr.send();
    };

    $btnDelete.click(handler);

    //show dialo
    $modal.modal('show');

}

//ExerciseRegistration
function registerExercise(date){
    var exerciseAnchor = document.getElementsByClassName("list-group-item active")[0];
    var time = document.getElementById("time-input");
    var weight = document.getElementById("weight-input");
    var xhr = new XMLHttpRequest();
    var url = "/exercise/registration?registrationDate="
    xhr.onreadystatechange = function() { // listen for state changes
        if (xhr.readyState == 4 && xhr.status == 200) { // when completed we can move away
            window.location = url.concat(date);
        }
    }
    xhr.open("POST", url.concat(date), true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        exerciseId: exerciseAnchor.id,
        time: time.value,
        weight: weight.value
    }))
}

function openExerciseRegistrationEditModal(name, registrationId, date){
    //clone dialog and remove ids to ensure uniqueness
    //var $modal = $('#ModificationModal*').clone().removeAttr('id');
    var $originalModal = $('#ModificationModal')
    var $modal = $originalModal.clone().removeAttr('id');


    $modal.find('[name="modify-update-text"]').text(name);
    var $btnModify = $modal.find('[name="save-modification"]');

    var handler = function (){
        var $time = $modal.find('[name="edit-time-input"]');
        var $weight = $modal.find('[name="edit-weight-input"]');
        var xhr = new XMLHttpRequest();
        var urlForPut = "/exercise/registration/".concat(registrationId);
        var urlForGet = "/exercise/registration/".concat("?registrationDate=").concat(date);
        setXhrRedirect(xhr, urlForGet);

        xhr.open("PUT", urlForPut, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSON.stringify({
            time: $time.val(),
            weight: $weight.val()
        }))
    };

    $btnModify.click(handler);

    //show dialo
    $modal.modal('show');
}

function openExerciseRegistrationDeleteModal(name, registrationId, date){
    var $originalModal = $('#EliminationModal')
    var $modal = $originalModal.clone().removeAttr('id');
    $modal.find('[name="modify-delete-text"]').text("Are you sure you want to delete the registration of ".concat(name).concat("?"));
    var $btnDelete = $modal.find('[name="delete"]');

    var handler = function (){
        var xhr = new XMLHttpRequest();
        var urlForDelete = "/exercise/registration/".concat(registrationId);
        var urlForGet = "/exercise/registration/".concat("?registrationDate=").concat(date);

        setXhrRedirect(xhr, urlForGet);

        xhr.open("DELETE", urlForDelete, true);
        xhr.send();
    };

    $btnDelete.click(handler);

    //show dialo
    $modal.modal('show');

}