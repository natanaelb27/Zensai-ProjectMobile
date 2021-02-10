<?php
$databaseHost = 'localhost';
$databaseName = 'ZensaiDb';
$databaseUsername = 'root';
$databasePassword = '';

$mysqli = mysqli_connect($databaseHost, $databaseUsername, $databasePassword, $databaseName) or die ("Koneksi Gagal"); 
mysqli_select_db($mysqli,$databaseName) or die ("Database belum siap!");

?>