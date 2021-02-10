<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $id_iklan = $_POST['id_iklan'];
   $status = $_POST['status'];
  $tgl_tayang = date('Y-m-d');
  $tgl_turun = date('Y-m-d',strtotime('+7 days'));
   if($status == 'approved'){
    $qupdate=mysqli_query($mysqli,"UPDATE mst_iklan SET tgl_tayang = '$tgl_tayang', tgl_turun = '$tgl_turun', status = '$status' WHERE id_iklan = '$id_iklan'");
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
   }else if($status == 'refused'){
    $qupdate=mysqli_query($mysqli,"UPDATE mst_iklan SET status = '$status' WHERE id_iklan = '$id_iklan'");
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