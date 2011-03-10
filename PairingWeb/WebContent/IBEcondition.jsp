<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type"
	content="application/xhtml+xml; charset=Shift_JIS" />
<meta name="keywords" content="キーワード" />
<meta name="description" content="紹介文" />
<title>Pairing crypto</title>
<link rel="stylesheet" href="style.css" type="text/css" />
</head>

<body>

<!-- メイン -->

<div id="main"><!-- ヘッダー -->

<div id="header">

<h1><a href="index.html">Pairing Crypto</a></h1>

</div>


<div id="header-menu">
<ul>
	<li><a href="index.html">HOME</a></li>
	<li><a href="aboutPairing.html">Pairing 暗号とは？</a></li>
	<li><a href="idbase.html">IDベース暗号</a></li>
	<li><a href="short.html">ショート署名</a></li>
	<li><a href="#">三者間鍵共有</a></li>
	<li><a href="QandA.html">よくあるご質問</a></li>
	<li><a href="map.html">サイトマップ</a></li>
	<li><a href="address">お問い合わせ</a></li>
</ul>
</div>

<!-- ヘッダー終わり --> <!-- コンテンツ --> <!-- メインコンテンツ -->

<div id="container">
<div id="contents">

<h1>現在提供しているIBEペアリング暗号の公開鍵コンディション</h1>
<p>
ビット数：<%= Integer.parseInt(request.getAttribute("m").toString())*4 %><br />
既約多項式：x^<%=request.getAttribute("m") %> + x^<%=request.getAttribute("k") %>+1 <br />
楕円曲線式:y^2+y=x^3 +(<%=request.getAttribute("ae") %>)x+<%=request.getAttribute("be") %> <br />
<%
 	String Px = request.getAttribute("Px").toString();
	String Py = request.getAttribute("Py").toString();
	String sPx = request.getAttribute("sPx").toString();
	String sPy = request.getAttribute("sPy").toString();
%>
P = 
(	<%="\t" + Px.substring(0,Px.length()/2)%>
	<%="\t\t" + Px.substring(Px.length()/2, Px.length()) %>
,
	<%="\t" + Py.substring(0,Py.length()/2)%>
	<%="\t \t" + Py.substring(Py.length()/2, Py.length()) %>)<br />
sP = 
(	<%="\t" + sPx.substring(0,sPx.length()/2)%>
	<%="\t \t" + sPx.substring(sPx.length()/2, sPx.length()) %>
,
	<%="\t" + sPy.substring(0,sPy.length()/2)%>
	<%="\t \t" + sPy.substring(sPy.length()/2, sPy.length()) %>)
</p>

</div>

<!-- メインコンテンツ終わり --> <!-- メニュー -->

<div id="menu">

<h1>IDベース暗号</h1>
<p><a href="PKGserver">Pairingコンディション</a><br />
<a href="IBEkey.html">鍵取得</a><br />
</p>

<h1>プロジェクトリンク</h1>
<p><a href="http://www.tsukuba.ac.jp/">筑波大学</a><br />
<a href="http://www.risk.tsukuba.ac.jp/">筑波大学大学院 リスク工学専攻</a><br />
<a href="http://www.cipher.risk.tsukuba.ac.jp/">暗号・情報セキュリティ研究室・岡本研究室</a><br />
<a href="http://www.coins.tsukuba.ac.jp/">情報学群 情報科学類・情報学類</a><br />
</p>


<h1>Pairing関連リンク</h1>
<p><a href="http://eprint.iacr.org/index.html">Cryptology ePrint Archive</a><br />
<a href="css2008.pdf">CSS2008発表論文</a><br />
<a href="CSS etaT.pdf">CSS2008発表スライド</a><br />
</p>

</div>

<!-- メニュー終わり --></div>

<!-- コンテンツ終わり --> <!-- フッター -->

<div id="footer">Copyright (C) 2008 SITE NAME All Rights Reserved.
design by <a href="http://tempnate.com">tempnate</a></div>

<!-- フッター終わり --></div>

<!-- メイン終わり -->

</body>
</html>
