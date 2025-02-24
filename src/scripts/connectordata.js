function editConnectorData(connector, apiUrl, username, password, project, documentSelection, startDate, tenantId, clientId, clientSecret, siteName, uploadFile, repoUrl, branchName, repository, streamName, component) {
    document.getElementById('editConnector').value = connector;
    document.getElementById('editApiUrl').value = apiUrl;
    document.getElementById('editUsername').value = username;
    document.getElementById('editPassword').value = password;
    document.getElementById('editProject').value = project;
    document.getElementById('editDocumentSelection').value = documentSelection;
    document.getElementById('editStartDate').value = startDate;
    document.getElementById('editTenantId').value = tenantId;
    document.getElementById('editClientId').value = clientId;
    document.getElementById('editClientSecret').value = clientSecret;
    document.getElementById('editSiteName').value = siteName;
    document.getElementById('editUploadFile').value = uploadFile;
    document.getElementById('editRepoUrl').value = repoUrl;
    document.getElementById('editBranchName').value = branchName;
    document.getElementById('editRepository').value = repository;
    document.getElementById('editStreamName').value = streamName;
    document.getElementById('editComponent').value = component;
    new bootstrap.Modal(document.getElementById('editModal')).show();
}

function showDeleteModal() {
    new bootstrap.Modal(document.getElementById('deleteModal')).show();
}

$(document).ready(function() {
    $('#connectorDataTable').DataTable({
        dom: 'Bfrtip',
        buttons: [
            'copyHtml5', 'csvHtml5', 'pdfHtml5'
        ]
    });
});