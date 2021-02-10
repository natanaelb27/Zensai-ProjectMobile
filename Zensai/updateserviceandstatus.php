<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $service = $_POST['service'];
    $username = $_POST['username'];

    $qcust=mysqli_query($mysqli,"SELECT id_cust FROM mst_customer WHERE username = '$username'");
    if(mysqli_num_rows($qcust)>0){
      while($r = mysqli_fetch_array($qcust)){   
        $id_cust = $r['id_cust'];
      }
    }

    $qcart=mysqli_query($mysqli,"SELECT * FROM tbl_cart WHERE id_cust = '$id_cust' AND status = 'pending'");
    if($qcart){  
      $qupdate=mysqli_query($mysqli,"UPDATE tbl_cart SET service = '$service', status = 'wait' WHERE id_cust = '$id_cust' AND status = 'pending'");
      $response = array();
      if($qupdate){
        $response["success"] =1;
        $response["message"] = "Data berhasil ditambah";
        echo json_encode($response);
      }
      else{
        $response["success"] =0;
        $response["message"] = "Data gagal ditambah";
        echo json_encode($response);
      }
      
    } 


	  
?>	