<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/REC-html40/loose.dtd">
<html>

<!-- $Id:$ -->

<head>
<script language="javascript" type="text/javascript" src="ajax.js"></script>
<title>XML Transaction Service 1.1 Demo Application Client</title>
</head>

<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" vlink="#336699" alink="#003366" link="#003366" text="#000000" bgcolor="#ffffff">

<!-- logo and header text -->
<table width="740" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td colspan="2" height="28">&nbsp;</td>
  </tr>
  <tr>
    <td width="20">&nbsp;</td>
    <td align="center" width="170" bgcolor="#ffffff">
      <img src="images/JBoss_byRH_logo_rgb.png" alt="jboss logo" border="0">
    </td>
    <td width="20">&nbsp;</td>
    <td valign="middle" align="left" width="530" bgcolor="#ffffff">
      <br/>
      <font size="5" style="font-family: Arial, Helvetica, sans-serif">
        XML Transaction Service 1.1 Demonstrator Application
      </font>
    </td>
  </tr>
</table>


<TABLE width="740" cellpadding="0" cellspacing="0" border="0">
<TR>
<TD width="20">&nbsp;</TD><TD valign="top" width="170">

<!-- nav menu -->

<p></p>
</TD><TD width="20">&nbsp;</TD><TD valign="top" width="530">

<form method="post" action="client">

<% if(null != request.getAttribute("result")) { %>
<!-- tx result panel -->
<TABLE width="530" cellpadding="0" cellspacing="0" border="0">
<TR>
<TD colspan="3" height="20" bgcolor="#ffffff">&nbsp;</TD>
</TR>
<TR>
<TD valign="top" align="left" width="10" bgcolor="#336699"><IMG src="images/tl_corner_10.gif" alt="" border="0"></TD><TD valign="middle" align="left" width="510" bgcolor="#336699"><DIV class="result_title"><FONT size="2" color="#ffffff" style="font-family: Arial, Helvetica, sans-serif"><B>Transaction Result</B></FONT></DIV></TD><TD valign="top" align="right" width="10" bgcolor="#336699"><IMG src="images/tr_corner_10.gif" alt="" border="0"></TD>
</TR>
<TR>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD><TD width="510" bgcolor="#e3e3e3">
<FONT size="2" style="font-family: Arial, Helvetica, sans-serif">
<DIV >
<p id="result"">


<%= request.getAttribute("result") %>
</p>
</DIV>
<DIV class="result">
<p>
<a href="javascript:loadContent()" ">Get result</a>
</p>
</DIV>
</FONT></TD>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD>
</TR>
<TR>
<TD valign="bottom" align="left" width="10" bgcolor="#e3e3e3"><IMG src="images/bl_corner_10.gif" alt="" border="0"></TD><TD width="510" bgcolor="#e3e3e3">&nbsp;</TD><TD valign="bottom" align="right" width="10" bgcolor="#e3e3e3"><IMG src="images/br_corner_10.gif" alt="" border="0"></TD>
</TR>
</TABLE>
<% } // end if %>


<!-- transaction type selection panel -->
<TABLE width="530" cellpadding="0" cellspacing="0" border="0">
<TR>
<TD colspan="3" height="20" bgcolor="#ffffff">&nbsp;</TD>
</TR>
<TR>
<TD valign="top" align="left" width="10" bgcolor="#336699"><IMG src="images/tl_corner_10.gif" alt="" border="0"></TD><TD valign="middle" align="left" width="510" bgcolor="#336699"><FONT size="2" color="#ffffff" style="font-family: Arial, Helvetica, sans-serif"><B>Restaurant Service - Booking Form</B></FONT></TD><TD valign="top" align="right" width="10" bgcolor="#336699"><IMG src="images/tr_corner_10.gif" alt="" border="0"></TD>
</TR>
<TR>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD><TD width="510" bgcolor="#e3e3e3">
<FONT size="2" style="font-family: Arial, Helvetica, sans-serif">
<div>
<p>
Transaction Type:
<SELECT NAME="txType" ID="txType">
<option value="AtomicTransaction">Atomic Transaction</option>
<!--  <option value="BusinessActivity">Business Activity</option>-->
</SELECT>
</p>
</FONT></TD>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD>
</TR>
<TR>
<TD valign="bottom" align="left" width="10" bgcolor="#e3e3e3"><IMG src="images/bl_corner_10.gif" alt="" border="0"></TD><TD width="510" bgcolor="#e3e3e3">&nbsp;</TD><TD valign="bottom" align="right" width="10" bgcolor="#e3e3e3"><IMG src="images/br_corner_10.gif" alt="" border="0"></TD>
</TR>
</TABLE>


<!-- restaurant booking panel -->
<TABLE width="530" cellpadding="0" cellspacing="0" border="0">
<TR>
<TD colspan="3" height="20" bgcolor="#ffffff">&nbsp;</TD>
</TR>
<TR>
<TD valign="top" align="left" width="10" bgcolor="#336699"><IMG src="images/tl_corner_10.gif" alt="" border="0"></TD><TD valign="middle" align="left" width="510" bgcolor="#336699"><FONT size="2" color="#ffffff" style="font-family: Arial, Helvetica, sans-serif"><B>Restaurant Service - Booking Form</B></FONT></TD><TD valign="top" align="right" width="10" bgcolor="#336699"><IMG src="images/tr_corner_10.gif" alt="" border="0"></TD>
</TR>
<TR>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD><TD width="510" bgcolor="#e3e3e3">
<FONT size="2" style="font-family: Arial, Helvetica, sans-serif">
<div>
<p>
Table for
<SELECT NAME="restaurant" ID="restaurant">
<OPTION>1
<OPTION>2
<OPTION>3
<OPTION>4
<OPTION>5
<OPTION>6
<OPTION>7
<OPTION>8
<OPTION>9
<OPTION>10
</SELECT>
people.
</p>
</FONT></TD>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD>
</TR>
<TR>
<TD valign="bottom" align="left" width="10" bgcolor="#e3e3e3"><IMG src="images/bl_corner_10.gif" alt="" border="0"></TD><TD width="510" bgcolor="#e3e3e3">&nbsp;</TD><TD valign="bottom" align="right" width="10" bgcolor="#e3e3e3"><IMG src="images/br_corner_10.gif" alt="" border="0"></TD>
</TR>
</TABLE>

<!-- theatre booking panel -->
<TABLE width="530" cellpadding="0" cellspacing="0" border="0">
<TR>
<TD colspan="3" height="20" bgcolor="#ffffff">&nbsp;</TD>
</TR>
<TR>
<TD valign="top" align="left" width="10" bgcolor="#336699"><IMG src="images/tl_corner_10.gif" alt="" border="0"></TD><TD valign="middle" align="left" width="510" bgcolor="#336699"><FONT size="2" color="#ffffff" style="font-family: Arial, Helvetica, sans-serif"><B>Theatre Service - Booking Form</B></FONT></TD><TD valign="top" align="right" width="10" bgcolor="#336699"><IMG src="images/tr_corner_10.gif" alt="" border="0"></TD>
</TR>
<TR>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD><TD width="510" bgcolor="#e3e3e3">
<FONT size="2" style="font-family: Arial, Helvetica, sans-serif">
<div>
<p>
Book
<SELECT NAME="theatrecirclecount" ID="theatrecirclecount">
<OPTION>0
<OPTION>1
<OPTION>2
<OPTION>3
<OPTION>4
<OPTION>5
<OPTION>6
<OPTION>7
<OPTION>8
<OPTION>9
<OPTION>10
</SELECT>
seats in the Circle
</p>
<p>
Book
<SELECT NAME="theatrestallscount" ID="theatrestallscount">
<OPTION>0
<OPTION>1
<OPTION>2
<OPTION>3
<OPTION>4
<OPTION>5
<OPTION>6
<OPTION>7
<OPTION>8
<OPTION>9
<OPTION>10
</SELECT>
seats in the Stalls
</p>
<p>
Book
<SELECT NAME="theatrebalconycount" ID="theatrebalconycount">
<OPTION>0
<OPTION>1
<OPTION>2
<OPTION>3
<OPTION>4
<OPTION>5
<OPTION>6
<OPTION>7
<OPTION>8
<OPTION>9
<OPTION>10
</SELECT>
seats in the Balcony
</p>
</FONT></TD>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD>
</TR>
<TR>
<TD valign="bottom" align="left" width="10" bgcolor="#e3e3e3"><IMG src="images/bl_corner_10.gif" alt="" border="0"></TD><TD width="510" bgcolor="#e3e3e3">&nbsp;</TD><TD valign="bottom" align="right" width="10" bgcolor="#e3e3e3"><IMG src="images/br_corner_10.gif" alt="" border="0"></TD>
</TR>
</TABLE>

<!-- taxi booking panel -->
<TABLE width="530" cellpadding="0" cellspacing="0" border="0">
<TR>
<TD colspan="3" height="20" bgcolor="#ffffff">&nbsp;</TD>
</TR>
<TR>
<TD valign="top" align="left" width="10" bgcolor="#336699"><IMG src="images/tl_corner_10.gif" alt="" border="0"></TD><TD valign="middle" align="left" width="510" bgcolor="#336699"><FONT size="2" color="#ffffff" style="font-family: Arial, Helvetica, sans-serif"><B>Taxi Service - Booking Form</B></FONT></TD><TD valign="top" align="right" width="10" bgcolor="#336699"><IMG src="images/tr_corner_10.gif" alt="" border="0"></TD>
</TR>
<TR>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD><TD width="510" bgcolor="#e3e3e3">
<FONT size="2" style="font-family: Arial, Helvetica, sans-serif">
<div>
<p>
Book a taxi?
<SELECT NAME="taxi" ID="taxi">
<option value="0">No</option>
<option value="1">Yes</option>
</SELECT>
</p>
</FONT></TD>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD>
</TR>
<TR>
<TD valign="bottom" align="left" width="10" bgcolor="#e3e3e3"><IMG src="images/bl_corner_10.gif" alt="" border="0"></TD><TD width="510" bgcolor="#e3e3e3">&nbsp;</TD><TD valign="bottom" align="right" width="10" bgcolor="#e3e3e3"><IMG src="images/br_corner_10.gif" alt="" border="0"></TD>
</TR>
</TABLE>

<!-- submit / reset panel -->
<TABLE width="530" cellpadding="0" cellspacing="0" border="0">
<TR>
<TD colspan="3" height="20" bgcolor="#ffffff">&nbsp;</TD>
</TR>
<TR>
<TD valign="top" align="left" width="10" bgcolor="#336699"><IMG src="images/tl_corner_10.gif" alt="" border="0"></TD><TD valign="middle" align="left" width="510" bgcolor="#336699"><FONT size="2" color="#ffffff" style="font-family: Arial, Helvetica, sans-serif"><B>Booking Controls</B></FONT></TD><TD valign="top" align="right" width="10" bgcolor="#336699"><IMG src="images/tr_corner_10.gif" alt="" border="0"></TD>
</TR>
<TR>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD><TD width="510" bgcolor="#e3e3e3">
<FONT size="2" style="font-family: Arial, Helvetica, sans-serif">
<p>
<input type="submit" name="submit" id="submit" value="Submit Booking Requests" />
&nbsp;&nbsp;
<input type="reset" name="reset" id="reset" value="Reset Form Values" />
</p>
</FONT></TD>
<TD width="10" bgcolor="#e3e3e3">&nbsp;</TD>
</TR>
<TR>
<TD valign="bottom" align="left" width="10" bgcolor="#e3e3e3"><IMG src="images/bl_corner_10.gif" alt="" border="0"></TD><TD width="510" bgcolor="#e3e3e3">&nbsp;</TD><TD valign="bottom" align="right" width="10" bgcolor="#e3e3e3"><IMG src="images/br_corner_10.gif" alt="" border="0"></TD>
</TR>
</TABLE>

</form>

</table>

</body>

</html>

