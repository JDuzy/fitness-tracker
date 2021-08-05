/*//registration.html
$('.text-start').on('submit', function () {
    alert('Form submitted!');
    var weightInput = document.getElementById("weightInput")
    var currentDate = getDate()
    registerWeight(date, weight)
});*/

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

//WeightRegistration
function registerWeight(date){
    var weight = document.getElementById("weight-input");
    var xhr = new XMLHttpRequest();
    var url = "/weight/registration?registrationDate="
    xhr.onreadystatechange = function() { // listen for state changes
        if (xhr.readyState == 4 && xhr.status == 200) { // when completed we can move away
            window.location = url.concat(date);
        }
    }
    xhr.open("POST", url.concat(date), true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        weight: weight
    }))
    return
}

function registerWeight(date){
    var foodAnchor = document.getElementsByClassName("list-group-item active")[0];
    var weight = document.getElementById("weight-input");
    var xhr = new XMLHttpRequest();
    var url = "/weight/registration?registrationDate="
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

//FoodRegistration
function registerFood(date){
    var foodAnchor = document.getElementsByClassName("list-group-item active")[0];
    var inputs = document.getElementsByName("amount-input");
    var amount = null;
    for (let input of inputs){
        if (input.value != ""){
            amount = input.value
        }
    }
    var xhr = new XMLHttpRequest();
    var url = "/food/registration?registrationDate=".concat(date)
    setXhrRedirect(xhr, url)
    /*xhr.onreadystatechange = function() { // listen for state changes
        if (xhr.readyState == 4 && xhr.status == 200) { // when completed we can move away
            window.location = url.concat(date);
        }
    }*/
    xhr.open("POST", url, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        foodId: foodAnchor.getAttribute("value"),
        amount: amount
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
    var times = document.getElementsByName("time-input");
    var weights = document.getElementsByName("weight-input");
    var timeMinutes = null;
    var weightKilograms = null;
    var xhr = new XMLHttpRequest();
    var url = "/exercise/registration?registrationDate="

    for (let time of times){
        if (time.value != ""){
            timeMinutes = time.value
        }
    }
    for (let weight of weights){
        if (weight.value != ""){
            weightKilograms = weight.value
        }
    }

    xhr.onreadystatechange = function() { // listen for state changes
        if (xhr.readyState == 4 && xhr.status == 200) { // when completed we can move away
            window.location = url.concat(date);
        }
    }
    xhr.open("POST", url.concat(date), true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        exerciseId: exerciseAnchor.getAttribute("value"),
        time: timeMinutes,
        weight: weightKilograms
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

//Weight
function registerWeight(date){
    var weights = document.getElementsByName("weight-input");
    var xhr = new XMLHttpRequest();
    var url = "/weight/registration?registrationDate="
    var weightInKg = null;

    for (let weight of weights){
        if (weight.value != ""){
            weightInKg = weight.value;
        }
    }
    xhr.onreadystatechange = function() { // listen for state changes
        if (xhr.readyState == 4 && xhr.status == 200) { // when completed we can move away
            window.location = url.concat(date);
        }
    }
    xhr.open("POST", url.concat(date), true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        weight: weightInKg
    }))
}

function openWeightRegistrationModal(date){
    //clone dialog and remove ids to ensure uniqueness
    //var $modal = $('#ModificationModal*').clone().removeAttr('id');
    var $originalModal = $('#WeightRegistrationModal')
    var $modal = $originalModal.clone().removeAttr('id');

    $modal.find('[name="registration-form"]')//establish value as date?
    $modal.find('[name="modify-update-text"]').text(name);
    var $btnModify = $modal.find('[name="save-registration"]');

    var register = function(){registerWeight(date)}

    $btnModify.click(register);

    //show dialo
    $modal.modal('show');
}

function openWeightRegistrationEditModal(date, registrationId){
    //clone dialog and remove ids to ensure uniqueness
    //var $modal = $('#ModificationModal*').clone().removeAttr('id');
    var $originalModal = $('#WeightRegistrationEditModal')
    var $modal = $originalModal.clone().removeAttr('id');

    $modal.find('[name="registration-form"]')//establish value as date?
    $modal.find('[name="modify-update-text"]').text(name);
    var $btnModify = $modal.find('[name="modify-registration"]');
    var $btnDelete = $modal.find('[name="delete-registration"]');

    var modifyWeightRegistration = function (){
        var $weight = $modal.find('[name="edit-weight-input"]');
        var xhr = new XMLHttpRequest();
        var urlForPut = "/weight/registration/".concat(registrationId);
        var urlForGet = "/weight/registration/".concat("?registrationDate=").concat(date);
        setXhrRedirect(xhr, urlForGet);

        xhr.open("PUT", urlForPut, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSON.stringify({
            weight: $weight.val()
        }))
    };

    var deleteWeightRegistration = function (){
        var xhr = new XMLHttpRequest();
        var urlForDelete = "/weight/registration/".concat(registrationId);
        var urlForGet = "/weight/registration/".concat("?registrationDate=").concat(date);

        setXhrRedirect(xhr, urlForGet);

        xhr.open("DELETE", urlForDelete, true);
        xhr.send();
    };

    $btnModify.click(modifyWeightRegistration)
    $btnDelete.click(deleteWeightRegistration);

    //show dialo
    $modal.modal('show');
}
