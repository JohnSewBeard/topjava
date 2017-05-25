var ajaxUrl = 'ajax/admin/users/';
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $('#datatable').DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
    filter = null;
});

function changeStatus(checkbox, userId) {
    var status = checkbox.checked;
    $.ajax({
        url: ajaxUrl + userId,
        type: 'Post',
        cache: false,
        data: 'status=' + status,
        success: function () {
            $(checkbox).closest('tr').toggleClass('danger');
            successNoty(status ? 'Enabled' : 'Disabled');
        }
    });
}