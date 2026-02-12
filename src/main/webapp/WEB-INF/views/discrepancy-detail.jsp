<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Discrepancy Detail</title>
</head>
<body>

<h2>Discrepancy Detail</h2>

<%
    model.InventoryDiscrepancy d =
        (model.InventoryDiscrepancy) request.getAttribute("discrepancy");
%>

<p>SKU: <%= d.getSku() %></p>
<p>Warehouse: <%= d.getWarehouseId() %></p>
<p>Expected: <%= d.getExpectedQuantity() %></p>
<p>Actual: <%= d.getActualQuantity() %></p>
<p>Status: <%= d.getStatus() %></p>

<h3>State History</h3>
<table border="1">
<tr>
    <th>From</th>
    <th>To</th>
    <th>By</th>
    <th>Reason</th>
    <th>At</th>
</tr>

<%
    java.util.List history =
        (java.util.List) request.getAttribute("history");

    for (Object obj : history) {
        java.util.Map row = (java.util.Map) obj;
%>
<tr>
    <td><%= row.get("previous_status") %></td>
    <td><%= row.get("new_status") %></td>
    <td><%= row.get("changed_by") %></td>
    <td><%= row.get("change_reason") %></td>
    <td><%= row.get("changed_at") %></td>
</tr>
<% } %>
</table>

<h3>Audit Log</h3>
<table border="1">
<tr>
    <th>Action</th>
    <th>Previous</th>
    <th>New</th>
    <th>By</th>
    <th>At</th>
</tr>

<%
    java.util.List audit =
        (java.util.List) request.getAttribute("audit");

    for (Object obj : audit) {
        java.util.Map row = (java.util.Map) obj;
%>
<tr>
    <td><%= row.get("action") %></td>
    <td><%= row.get("previous_value") %></td>
    <td><%= row.get("new_value") %></td>
    <td><%= row.get("performed_by") %></td>
    <td><%= row.get("performed_at") %></td>
</tr>
<% } %>
</table>

</body>
</html>
