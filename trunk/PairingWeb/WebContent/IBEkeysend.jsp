<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type"
	content="application/xhtml+xml; charset=Shift_JIS" />
<meta name="keywords" content="�L�[���[�h" />
<meta name="description" content="�Љ" />
<title>Pairing crypto</title>
<link rel="stylesheet" href="style.css" type="text/css" />
</head>

<body>

<!-- ���C�� -->

<div id="main"><!-- �w�b�_�[ -->

<div id="header">

<h1><a href="index.html">Pairing Crypto</a></h1>

</div>


<div id="header-menu">
<ul>
	<li><a href="index.html">HOME</a></li>
	<li><a href="aboutPairing.html">Pairing �Í��Ƃ́H</a></li>
	<li><a href="idbase.html">ID�x�[�X�Í�</a></li>
	<li><a href="short.html">�V���[�g����</a></li>
	<li><a href="#">�O�ҊԌ����L</a></li>
	<li><a href="QandA.html">�悭���邲����</a></li>
	<li><a href="map.html">�T�C�g�}�b�v</a></li>
	<li><a href="address">���₢���킹</a></li>
</ul>
</div>

<!-- �w�b�_�[�I��� --> <!-- �R���e���c --> <!-- ���C���R���e���c -->

<div id="container">
<div id="contents">

<h1>�閧���̔z�M</h1>
<p>
<%
	String ID = request.getAttribute("ID").toString();
 	boolean sent = new Boolean(request.getAttribute("send").toString());
%>
ID<%="\t" + ID%>����ւ̔閧���̑��M
<% if(sent){
	out.println("�͐������܂���");
	}else{
	out.println("�͎��s���܂���<br />���������[���A�h���X����͂��Ă�������");
	}
 %>
</p>

</div>

<!-- ���C���R���e���c�I��� --> <!-- ���j���[ -->

<div id="menu">

<h1>ID�x�[�X�Í�</h1>
<p><a href="PKGserver">Pairing�R���f�B�V����</a><br />
<a href="IBEkey.html">���擾</a><br />
</p>


<h1>�v���W�F�N�g�����N</h1>
<p><a href="http://www.tsukuba.ac.jp/">�}�g��w</a><br />
<a href="http://www.risk.tsukuba.ac.jp/">�}�g��w��w�@ ���X�N�H�w��U</a><br />
<a href="http://www.cipher.risk.tsukuba.ac.jp/">�Í��E���Z�L�����e�B�������E���{������</a><br />
<a href="http://www.coins.tsukuba.ac.jp/">���w�Q ���Ȋw�ށE���w��</a><br />
</p>


<h1>Pairing�֘A�����N</h1>
<p><a href="http://eprint.iacr.org/index.html">Cryptology ePrint Archive</a><br />
<a href="css2008.pdf">CSS2008���\�_��</a><br />
<a href="CSS etaT.pdf">CSS2008���\�X���C�h</a><br />
</p>

</div>

<!-- ���j���[�I��� --></div>

<!-- �R���e���c�I��� --> <!-- �t�b�^�[ -->

<div id="footer">Copyright (C) 2008 SITE NAME All Rights Reserved.
design by <a href="http://tempnate.com">tempnate</a></div>

<!-- �t�b�^�[�I��� --></div>

<!-- ���C���I��� -->

</body>
</html>
