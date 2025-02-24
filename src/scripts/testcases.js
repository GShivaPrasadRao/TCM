
$(document).ready(function() {
    $('#testCaseTable').DataTable({
        dom: 'Bfrtip',
        buttons: [
            'copyHtml5', 'csvHtml5', 'pdfHtml5'
        ]
    });
});