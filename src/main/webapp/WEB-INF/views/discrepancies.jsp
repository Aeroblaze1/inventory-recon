<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Discrepancies</title>
</head>
<body>

<h2>Active Discrepancies</h2>

<table border="1">
    <tr>
        <th>ID</th>
        <th>SKU</th>
        <th>Warehouse</th>
        <th>Expected</th>
        <th>Actual</th>
        <th>Difference</th>
        <th>Status</th>
        <th>Action</th>
    </tr>

    <%
        java.util.List list =
                (java.util.List) request.getAttribute("discrepancies");

        for (Object obj : list) {
            model.InventoryDiscrepancy d =
                    (model.InventoryDiscrepancy) obj;
    %>
    <tr>
        <td><%= d.getId() %></td>
        <td><%= d.getSku() %></td>
        <td><%= d.getWarehouseId() %></td>
        <td><%= d.getExpectedQuantity() %></td>
        <td><%= d.getActualQuantity() %></td>
        <td><%= d.getDifference() %></td>
        <td><%= d.getStatus() %></td>
        <td>
            <form method="post" action="workflow">
                <input type="hidden" name="id" value="<%= d.getId() %>" />
                <input type="submit" name="action" value="IN_REVIEW" />
                <input type="submit" name="action" value="RESOLVED" />
                <input type="submit" name="action" value="CLOSED" />
            </form>
        </td>
    </tr>
    <%
        }
    %>

</table>

</body>
</html>
