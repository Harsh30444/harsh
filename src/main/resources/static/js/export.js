
$(function () {
$('#example').DataTable({
dom: 'Bfrtip',
buttons: [{
extend: 'excel',
text: 'Excel',
className: 'exportExcel',
filename: 'Test_Excel',
exportOptions: { modifier: { page: 'all'} }
},
{
extend: 'pdf',
text: 'PDF',
className: 'exportExcel',
filename: 'Test_Pdf',
exportOptions: { modifier: { page: 'all'} }

},
     { extend: 'copy',
            text: 'Copy ',
            className: 'exportExcel',
            exportOptions: { modifier: { modifier: {    page: 'current'} }  }     
            },
{
extend: 'csv',
text: 'csv ',
exportOptions: {
modifier: {
search: 'none' } }
}]
});
});
