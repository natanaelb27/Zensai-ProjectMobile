<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 
 $image = $_POST['imageurl'];
 $username = $_POST['username'];                
 
$user = "root";
$pass = "";
$host= "localhost";
$dbname="Zensaidb";
header('Content-Type: bitmap; charset=utf-8');
$con = mysqli_connect($host,$user,$pass,$dbname);
 
/*$sql ="SELECT `id_iklan` FROM `cust_pp` ORDER BY id_iklan ASC";
 
$res = mysqli_query($con,$sql);
 
$id = 0;
 
 while($row = mysqli_fetch_array($res)){
 $id = $row['id'];
 }*/
 
 $id = uniqid($username, true);

 $p = "profilepic/$id.jpeg";
 
 $path = "https://10.0.2.2/Zensai/$p";
 

 //$sqli="INSERT INTO `mst_customer`( `linkprofilepic`)  values('".$path."');";
 
 $sqli="UPDATE `mst_customer` SET `linkprofilepic` ='".$path."' WHERE `username` = '".$username."';";
 
 if(mysqli_query($con,$sqli)){
 file_put_contents($p,base64_decode($image));
 echo "Successfully Uploaded";
 }
 
 mysqli_close($con);
 }else{
 echo "Error";
 }

?>