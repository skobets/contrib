<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib prefix="repox" uri="http://repox.ist.utl.pt/tlds/repox.tld"%>

<script type="text/javascript">
    function monitorDataSource(confirmationMessage, dataSourceId) {
        var Location = '/Homepage.action?monitorDataSource&dataSourceId=' + dataSourceId;
        confirmationHandling(confirmationMessage, Location);
    }

    function confirmationHandling(confirmationMessage, Location) {
        var answer = confirm (confirmationMessage);
        if (answer) {
            location.href = '${pageContext.request.contextPath}' + Location;
        }
        else {

        }
    }
</script>

<table class="mainTable">
    <thead>
    <tr>

        <th class="mainHeader"><img title="Country" alt="Country" src="${pageContext.request.contextPath}/jsp/images/worldmap.png" /></th>
        <th class="mainHeader"><fmt:message key="aggregator.id"/></th>
        <th class="mainHeader">
            <fmt:message key="aggregator.nameHeader"/>
            <stripes:link class="header" href="/springAggregator/createAggregator.html">
                ...<fmt:message key="common.create" />...
            </stripes:link>
        </th>
        <th class="mainHeader"><fmt:message key="dataProvider.id" /></th>
        <th class="mainHeader"><fmt:message key="dataProvider.nameHeader" /></th>
        <th class="mainHeader"><fmt:message key="dataSource.set" /></th>
        <th class="mainHeader"><fmt:message key="dataSource.oai.schemas" /></th>
        <th class="mainHeader"><fmt:message key="dataSource.ingest" /> <fmt:message key="common.type" /></th>
        <th class="mainHeader"><fmt:message key="common.last" /> <fmt:message key="dataSource.ingest" /> <fmt:message key="common.date" /></th>
        <th class="mainHeader"><fmt:message key="common.next" /> <fmt:message key="dataSource.ingest" /> <fmt:message key="common.date" /></th>
        <th class="mainHeader"><fmt:message key="dataSource.numberRecords" /></th>
        <th class="mainHeader"><fmt:message key="dataSource.ingestStatus" /></th>
    </tr>
    </thead>
    <c:forEach var="currentItem" items="${actionBean.pager.pageItems}" varStatus="currentIndex">
        <c:choose>
            <c:when test="${currentIndex.index % 2 == 0}"><c:set var="cellClass" value="even" /></c:when>
            <c:otherwise><c:set var="cellClass" value="odd" /></c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${currentItem.class.name == 'pt.utl.ist.repox.data.AggregatorRepox'}">
                <c:set var="aggregator" value="${currentItem}" />
                <tr>
                    <td class="${cellClass}">
                    </td>
                    <td class="${cellClass}" style="text-align:left;">
                        <c:out value="${aggregator.nameCode}"/>
                            <%--<c:out escapeXml="true" value="${aggregator.idDb}"/>--%>
                    </td>
                    <td class="${cellClass}" style="text-align:left;">
                        <stripes:link href="/springAggregator/viewAggregator.html?aggregatorId=${repox:encode(aggregator.id)}">
                            <c:out escapeXml="true" value="${aggregator.name}"/>
                        </stripes:link>
                    </td>
                    <td colspan="9" class="${cellClass}"></td>
                </tr>
            </c:when>
            <c:when test="${currentItem.class.name == 'pt.utl.ist.repox.data.DataProvider'}">
                <c:set var="dataProvider" value="${currentItem}" />
                <tr>
                    <td class="${cellClass}">
                        <c:if test="${!empty dataProvider.country}">
                            <c:set var="countryString" value="${fn:trim(dataProvider.country)}"/>
                            <img title="${dataProvider.country}" alt="${dataProvider.country}" src="${pageContext.request.contextPath}/jsp/images/countries/${repox:getCountries()[countryString]}.png" />
                        </c:if>
                    </td>
                    <td class="${cellClass}" style="text-align:left;">
                        <c:out value="${dataProvider.aggregator.nameCode}"/>
                    </td>
                    <td class="${cellClass}" style="text-align:left;">
                        <stripes:link href="/springAggregator/viewAggregator.html?aggregatorId=${repox:encode(dataProvider.aggregator.id)}">
                            <c:out value="${dataProvider.aggregator.name}"/>
                        </stripes:link>
                    </td>
                    <td class="${cellClass}" style="text-align:left;">
                        <c:out escapeXml="true" value="${dataProvider.nameCode}"/>
                    </td>
                    <td class="${cellClass}" style="text-align:left;">
                        <stripes:link href="/dataProvider/ViewDataProvider.action?dataProviderId=${repox:encode(dataProvider.id)}">
                            <c:out escapeXml="true" value="${dataProvider.name}"/>
                        </stripes:link>
                    </td>
                    <td colspan="7" class="${cellClass}"></td>
                </tr>
            </c:when>
            <c:otherwise>
                <!-- pt.utl.ist.repox.data.DataSource -->
                <c:set var="dataSource" value="${currentItem}" />

                <tr>
                    <td class="${cellClass}">
                        <c:if test="${!empty dataSource.dataProvider.country}">
                            <c:set var="countryString" value="${fn:trim(dataSource.dataProvider.country)}"/>
                            <img title="${dataSource.dataProvider.country}" alt="${dataSource.dataProvider.country}" src="${pageContext.request.contextPath}/jsp/images/countries/${repox:getCountries()[countryString]}.png" />
                        </c:if>
                    </td>
                    <td class="${cellClass}" style="text-align:left">
                        <c:out value="${dataSource.dataProvider.aggregator.nameCode}"/>
                    </td>
                    <td class="${cellClass}" style="text-align:left">
                        <stripes:link href="/springAggregator/viewAggregator.html?aggregatorId=${repox:encode(dataSource.dataProvider.aggregator.id)}">
                            <c:out value="${dataSource.dataProvider.aggregator.name}"/>
                        </stripes:link>
                    </td>
                    <td class="${cellClass}" style="text-align:left">
                        <c:out escapeXml="true" value="${dataSource.dataProvider.nameCode}"/>
                    </td>
                    <td class="${cellClass}" style="text-align:left">
                        <c:choose>
                            <c:when test="${actionBean.dataSourcesState[dataSource.id] == true}">
                                <img title="ok" alt="ok" src="${pageContext.request.contextPath}/jsp/images/ok.png" />
                            </c:when>
                            <c:when test="${actionBean.dataSourcesState[dataSource.id] == false}">
                                <c:set var="confirmationMessageMonitor"><fmt:message key="dataSource.monitor.confirmationMessageError" /></c:set>
                                <img class="action" alt="error" title="error" src="${pageContext.request.contextPath}/jsp/images/error.png"
                                     onclick="javascript:monitorDataSource('${confirmationMessageMonitor}', '${dataSource.id}');" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="confirmationMessageMonitor"><fmt:message key="dataSource.monitor.confirmationMessageUnknown" /></c:set>
                                <img class="action" alt="unknown" title="unknown" src="${pageContext.request.contextPath}/jsp/images/unknown.png"
                                     onclick="javascript:monitorDataSource('${confirmationMessageMonitor}', '${dataSource.id}');" />
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${dataSource.class.name == 'pt.utl.ist.repox.oai.DataSourceOai'}">
                            <stripes:link class="imageLink" href="/jsp/testOAI-PMH.jsp" target="_blank">
                                <stripes:param name="serverURL" value="${dataSource.oaiSourceURL}" />
                                <stripes:param name="set" value="${dataSource.oaiSet}" />
                                <stripes:param name="metadataPrefix" value="${dataSource.metadataFormat}" />

                                <img src="${pageContext.request.contextPath}/jsp/images/network-transmit.png" title="Test OAI-PMH" alt="Test OAI-PMH">
                            </stripes:link >
                        </c:if>

                        <stripes:link href="/dataProvider/ViewDataProvider.action?dataProviderId=${repox:encode(dataSource.dataProvider.id)}">
                            <c:out escapeXml="true" value="${dataSource.dataProvider.name}"/>
                        </stripes:link>
                    </td>

                    <c:set var="dataSource" value="${dataSource}" scope="request" />
                    <c:set var="cellClass" value="${cellClass}" scope="request" />
                    <c:import url="/jsp/dataSourceRow.include.jsp" />
                </tr>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</table>
<div class="center">
    <c:forEach var="pageReference" items="${actionBean.pager.pageReferences}" varStatus="currentIndex">
        <c:choose>
            <c:when test="${actionBean.pager.pageIndex == pageReference.value}">${pageReference.key}</c:when>
            <c:otherwise>
                <stripes:link href="?pageIndex=${pageReference.value}">${pageReference.key}</stripes:link>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>