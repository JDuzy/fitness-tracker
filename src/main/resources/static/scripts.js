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
        return
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
        return
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
        return
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
        return
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

//calendar
/*function createDaysFromLastMonth(date) {
    var dateTemp = moment(date, "YYYY-MM-DD")
    //var monthOfYear = dateTemp.get("month")//0-11
    //var year = dateTemp.get("year")
    var daysInMonth = dateTemp.daysInMonth(dateTemp)
    var x ="", i;
    for (i=1; i<=daysInMonth; i++) {
        x = x +
            '<li><div class="date">'+i+'</div></li>';
    }
    document.getElementById("calendar-days").innerHTML = x;
}*/

//utility
function getDate(){
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = mm + '/' + dd + '/' + yyyy;
    return today
}

