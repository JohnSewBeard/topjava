var ajaxUrl = 'ajax/profile/meals/';
var datatableApi;

$(function () {
    datatableApi = $('#mealDataTable').DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ]
    });
    makeEditable();
});
