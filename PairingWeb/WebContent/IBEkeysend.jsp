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

<h1>秘密鍵の配信</h1>
<p>
<%
	String ID = request.getAttribute("ID").toString();
 	boolean sent = new Boolean(request.getAttribute("send").toString());
%>
ID<%="\t" + ID%>さんへの秘密鍵の送信
<% if(sent){
	out.println("は成功しました");
	}else{
	out.println("は失敗しました<br />正しいメールアドレスを入力してください");
	}
 %>
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
