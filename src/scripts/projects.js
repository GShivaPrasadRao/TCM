function editProject(name, description) {
    document.getElementById('editProjectName').value = name;
    document.getElementById('editProjectDescription').value = description;
    new bootstrap.Modal(document.getElementById('editModal')).show();
}

function showDeleteModal() {
    new bootstrap.Modal(document.getElementById('deleteModal')).show();
}

$(document).ready(function() {
    $('#projectTable').DataTable({
        dom: 'Bfrtip',
        buttons: [
            'copyHtml5', 'csvHtml5', 'pdfHtml5'
        ]
    });
});