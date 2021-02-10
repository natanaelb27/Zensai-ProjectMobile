<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $username = $_POST['username'];

   $q=mysqli_query($mysqli,"SELECT * FROM mst_customer JOIN mst_userlogin ON mst_customer.username = mst_userlogin.username WHERE mst_customer.username='$username'");
   $response=array();
   
   if (mysqli_num_rows($q)>0)
   {
     $response["data"]=array();
     while($r=mysqli_fetch_array($q))
     {
     $user=array();
      $user["username"]=$r["username"];
      $user["namapelanggan"]=$r["namalengkap"];
      $user["email"]=$r["email"];
      $user["tanggallahir"]=$r["tgl_lahir"];
      $user["alamat"]=$r["alamat"];
      $user["nohp"]=$r["no_hp"];
      $user["profilepicture"]=$r["linkprofilepic"];
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