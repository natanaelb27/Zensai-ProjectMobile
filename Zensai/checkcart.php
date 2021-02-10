<?php
header('Content-type:application/json;charset=utf-8');
include "conn.php";


    $username = $_POST['username'];

    $qcust=mysqli_query($mysqli,"SELECT id_cust FROM mst_customer WHERE username = '$username'");
    if(mysqli_num_rows($qcust)>0){
      while($r = mysqli_fetch_array($qcust)){   
        $id_cust = $r['id_cust'];
      }
    }


    $qcheck=mysqli_query($mysqli,"SELECT * FROM tbl_cart WHERE id_cust = '$id_cust' AND status = 'approved'");
    if(mysqli_num_rows($qcheck)>0){
      $response["data"]=array();
      $response["success"] = 1;
      $response["message"] = "berhasil";
      echo json_encode($response);
    }else{
    }


?>