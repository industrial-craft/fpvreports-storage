function toggleFilterOptions() {
        const sortCriteria = document.getElementById("sortCriteria").value;
        const modelSelection = document.getElementById("modelSelectSection");
        const rebFilter = document.getElementById("rebFilter");
        const onTargetFilter = document.getElementById("isOnTargetFPVFilter");
        const rebSelect = document.getElementById("isLostFPVDueToREB");
        const onTargetSelect = document.getElementById("isOnTargetFPV");
        const submitButton = document.getElementById("submit-btn");

        modelSelection.style.display = sortCriteria === "fpvModel" ? "block" : "none";

        submitButton.textContent = (sortCriteria === "fpvModel" || sortCriteria === "isLostFPVDueToREB"
        || sortCriteria === "isOnTargetFPV") ? "Filter" : "Sort";

        if (sortCriteria === "isLostFPVDueToREB") {
            rebFilter.style.display = "block";
            rebSelect.disabled = false;
        } else {
            rebFilter.style.display = "none";
            rebSelect.disabled = true;
        }

        if (sortCriteria === "isOnTargetFPV") {
            onTargetFilter.style.display = "block";
            onTargetSelect.disabled = false;
        } else {
            onTargetFilter.style.display = "none";
            onTargetSelect.disabled = true;
        }

        console.log("Sort Criteria in toggleFilterOptions: " + sortCriteria)

}

/*function updateFilterCriteria() {
    const selectedModel = document.getElementById("fpvModel").value;
    console.log("FPV Model Filter: " + selectedModel)
    const sortCriteriaValue = document.getElementById("sortCriteria").value;
    const sortCriteriaName = document.getElementById("sortCriteria").name;
    console.log("Sort Criteria Value in updateFilterCriteria: " + sortCriteriaValue)
    console.log("Sort Criteria Name in updateFilterCriteria: " + sortCriteriaName)

    if (sortCriteria === "fpvModel") {
    document.getElementById("sortCriteria").name = "sortCriteria";
    //document.getElementById("sortCriteria").value = `selectedModel`;
    }
}*/
/*
function updateSortCriteria() {
    const sortCriteria = document.getElementById("sortCriteria").value;
    const modelSelection = document.getElementById("modelSelectSection");
    //const rebFilter = document.getElementById("rebFilter");
    const fpvModelFilter = document.getElementById("fpvModel").value;
    //const isLostFPVDueToREBFilter = document.getElementById("isLostFPVDueToREB").value;

     modelSelection.style.display = sortCriteria === "fpvModel" ? "block" : "none";
     //rebFilter.style.display = sortCriteria === "isLostFPVDueToREB" ? "block" : "none";

    document.getElementById("sortCriteria").value = `fpvModel:${fpvModelFilter}:`;


    if (sortCriteria === "fpvModel") {
       // document.getElementById("modelFilter").style.display = "block";
       // document.getElementById("rebFilter").style.display = "none";
        document.getElementById("sortCriteria").name = "sortCriteria";
        document.getElementById("sortCriteria").value = `fpvModel:${fpvModelFilter}:`;
    } else if (sortCriteria === "isLostFPVDueToREB") {
       // document.getElementById("modelFilter").style.display = "none";
       // document.getElementById("rebFilter").style.display = "block";
        document.getElementById("sortCriteria").name = "sortCriteria";
        document.getElementById("sortCriteria").value = `isLostFPVDueToREB:${isLostFPVDueToREBFilter}:`;
    } else {
        document.getElementById("modelSelection").style.display = "none";
        document.getElementById("rebFilter").style.display = "none";
    }

        //rebFilter.style.display = sortCriteria === "isLostFPVDueToREB" ? "block" : "none";
/*        if (sortCriteria === "fpvModel") {
        document.getElementById("sortCriteria").value = "sortCriteria";
        document.getElementById("sortCriteria").value = `fpvModel:${fpvModelFilter}:`;
        }*/
        //submitButton.textContent = (sortCriteria === "fpvModel" || sortCriteria === "isLostFPVDueToREB") ? "Filter" : "Sort";

