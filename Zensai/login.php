<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   //$_POST['username'] = "admincs";
   //$_POST['password'] = "adminst";

   $username = $_POST['username'];
   $password = $_POST['password'];
   $q=mysqli_query($mysqli,"select * from mst_userlogin where username='$username' AND password ='$password'");
   $response=array();
   
   if (mysqli_num_rows($q)>0)
   {
	  $response["data"]=array();
	  while($r=mysqli_fetch_array($q))
	  {
	  $user=array();
	  $user["username"]=$r["username"];
	  //$user["password"]=$r["password"];
      //$user["email"]=$r["email"];
      $user["level_otoritas"]=$r["level_otoritas"];
	  array_push($response["data"],$user);
	  }
      $response["success"]=1;
	  $response["message"]="data berhasil diambil";
      echo json_encode($response);
   }
   else
   { 
	  $response["success"]=-1;
	  $response["message"]="data kosong";
	  echo json_encode($response);

	}
	  
?>	