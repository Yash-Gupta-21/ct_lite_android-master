package com.i9930.croptrails.Utility;

import com.google.gson.JsonObject;
import com.i9930.croptrails.AddFarm.Model.FarmerAndFarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerFarmInsertResponse;
import com.i9930.croptrails.AddFarm.Model.FetchFarmerResponse;
import com.i9930.croptrails.AddFarm.Model.IfscResponse;
import com.i9930.croptrails.AddFarm.Model.SupervisorListResponse;
import com.i9930.croptrails.AgriInput.Model.FarmAgriInputResponse;
import com.i9930.croptrails.AssignCalendar.Model.CalendarFetchResponse;
import com.i9930.croptrails.AssignCalendar.Model.SingleCalendarResponse;
import com.i9930.croptrails.CommonClasses.Address.CityResponse;
import com.i9930.croptrails.CommonClasses.Address.CountryResponse;
import com.i9930.croptrails.CommonClasses.Address.StateResponse;
import com.i9930.croptrails.CommonClasses.ClusterResponse;
import com.i9930.croptrails.CommonClasses.DDResponseNew;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormResponse;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.Season.SeasonResponse;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.Communication.Model.CaseListResponse;
import com.i9930.croptrails.Communication.Model.InsertCaseResponse;
import com.i9930.croptrails.CompSelect.Model.CompanyResponse;
import com.i9930.croptrails.CropCycle.Create.Model.FetchType.ActivityTypeResponse;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.FarmDetailsUpdateSendData;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.PersonFullResponse;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.SpinnerResponse;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.UpdatRsponse;
import com.i9930.croptrails.FarmDetails.Model.FullDetailsResponse;
import com.i9930.croptrails.FarmDetails.Model.HarvestAndFloweringSendData;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineResponse;
import com.i9930.croptrails.FarmerInnerDashBoard.Models.FarmerInnerPagerResponse;
import com.i9930.croptrails.GerminationAndSpacing.Model.SendGerminationSpacingData;
import com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model.SampleGerminationStatusNdData;
import com.i9930.croptrails.HarvestCollection.Model.HarvestCollectionMainData;
import com.i9930.croptrails.HarvestCollection.Model.HarvestSendPlannedData;
import com.i9930.croptrails.HarvestCollection.Model.HarvestSubmitData;
import com.i9930.croptrails.HarvestPlan.Model.ShowHarvestPlanMain;
import com.i9930.croptrails.Maps.SvFarm.SvMapFarmResponse;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.GpsMainDataForSingleHarvest;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.SendDataForMapSingleHarvestPlan;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.StatusChangeCollectionData;
import com.i9930.croptrails.HarvestReport.Model.ViewHarvestDetails;
import com.i9930.croptrails.HarvestSubmit.Model.SendHarvestData;
import com.i9930.croptrails.Landing.Fragments.DashboardMapModel.MapResponse;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.DashboardResponse;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.SetCompIdClusterId;
import com.i9930.croptrails.Landing.Fragments.Model.AddExpenseResponse;
import com.i9930.croptrails.Landing.Fragments.Model.ExpenseData;
import com.i9930.croptrails.Landing.Fragments.Model.ExpenseRemoveResponse;
import com.i9930.croptrails.Landing.Fragments.Model.ProfileReciveMainData;
import com.i9930.croptrails.Landing.Models.FarmCountResponse;
import com.i9930.croptrails.Landing.Models.Farmer.FetchFarmerFarmResponse;
import com.i9930.croptrails.Landing.Models.FetchFarmData;
import com.i9930.croptrails.Landing.Models.FetchFarmSendData;
import com.i9930.croptrails.Landing.Models.Filter.ClusterVillageResponse;
import com.i9930.croptrails.Landing.Models.Filter.VillageChakResponse;
import com.i9930.croptrails.Landing.Models.GeoCardsResponse;
import com.i9930.croptrails.Landing.Models.NewData.FarmResponseNew;
import com.i9930.croptrails.Landing.Models.Offline.FarmVisitResponse;
import com.i9930.croptrails.Landing.Models.UpdateStandingArea.UpdateStandingResponse;
import com.i9930.croptrails.Landing.Models.Vetting.VettingFarmResponse;
import com.i9930.croptrails.Login.Model.CompParamResponse;
import com.i9930.croptrails.Login.Model.Post;
import com.i9930.croptrails.Maps.ShowArea.Model.GetGpsCoordinates;
import com.i9930.croptrails.Maps.ShowArea.Model.OmitanceShowResponse;
import com.i9930.croptrails.Maps.ShowArea.Model.WayPointFetchResponse;
import com.i9930.croptrails.Maps.ShowArea.Model.geojson.GeoJsonResponse;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.NearFarmResponse;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendOmtanceArea;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendWayPointData;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.VerifySendData;
import com.i9930.croptrails.Maps.WalkAround.FarmLocationData;
import com.i9930.croptrails.Maps.selectFarmerFarm.model.FarmerData;
import com.i9930.croptrails.Maps.selectFarmerFarm.model.SelectionResponse;
import com.i9930.croptrails.NoticeBoard.Model.NoticeResponse;
import com.i9930.croptrails.NoticeBoard.Model.SinglePostResponse;
import com.i9930.croptrails.Notification.Model.ShowNotificationResponse;
import com.i9930.croptrails.OfflineMode.Model.CalendarActResponse;
import com.i9930.croptrails.OfflineMode.Model.FarmIdArray;
import com.i9930.croptrails.OfflineMode.Model.FarmsCoordinatesResponse;
import com.i9930.croptrails.OfflineMode.Model.OfflineResponse;
import com.i9930.croptrails.Profile.Model.AddProfileSendData;
import com.i9930.croptrails.Profile.Model.ImageBody;
import com.i9930.croptrails.Profile.Model.ImageUpdateResponse;
import com.i9930.croptrails.QRGenerate.Model.QRResponse;
import com.i9930.croptrails.Query.Model.QueryParamResponse;
import com.i9930.croptrails.ResetPassword.Model.ResetPassSendData;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.ResponseCompReg;
import com.i9930.croptrails.RoomDatabase.FarmTable.SendOfflineFarm;
import com.i9930.croptrails.Scan.EncFarmResponse;
import com.i9930.croptrails.ServiceAndBroadcasts.Model.RecieveResponseGpsBack;
import com.i9930.croptrails.ServiceAndBroadcasts.Model.SendGpsArray;
import com.i9930.croptrails.ShowInputCost.Model.InputCostResponse;
import com.i9930.croptrails.SoilSense.SendSoilData;
import com.i9930.croptrails.SubmitActivityForm.Model.ActivityChemicalResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.ActivityUnitResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInputResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActivityResponseNew;
import com.i9930.croptrails.SubmitActivityForm.Model.SendTimelineActivityData;
import com.i9930.croptrails.SubmitActivityForm.Model.SendTimelineActivityDataNew;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardDDResponse;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardResponse;
import com.i9930.croptrails.SubmitHealthCard.Model.SendHealthCardData;
import com.i9930.croptrails.SubmitInputCost.Model.CostAndResourceSubmitData;
import com.i9930.croptrails.SubmitInputCost.Model.CostDropdownResponse;
import com.i9930.croptrails.SubmitInputCost.Model.CostSubmitResponse;
import com.i9930.croptrails.SubmitPld.Model.PldReasonResponse;
import com.i9930.croptrails.SubmitPld.Model.SendPldData;
import com.i9930.croptrails.SubmitVisitForm.Model.AddVisitSendDataNew;
import com.i9930.croptrails.SubmitVisitForm.Model.FetchVisitResponse;
import com.i9930.croptrails.Task.Model.Farm.SvFarmResponse;
import com.i9930.croptrails.Task.Model.TaskResponse;
import com.i9930.croptrails.Test.ChakModel.FetchChakResponse;
import com.i9930.croptrails.Test.PldModel.PldResponse;
import com.i9930.croptrails.Test.SatusreModel.SatsureResponse;
import com.i9930.croptrails.Test.SellModel.SellResponse;
import com.i9930.croptrails.Test.SoilSensResModel.SoilSenseResponse;
import com.i9930.croptrails.Test.model.full.FarmFullDetails;
import com.i9930.croptrails.Vetting.Model.OtherVettingFarmResponse;
import com.i9930.croptrails.Vetting.Model.VettingSearchBody;
import com.i9930.croptrails.Weather.Model.WeatherMainRes;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @POST("android_farm/get_irrigation_and_soil")
    Call<SpinnerResponse> getspinnerData(@Body JsonObject jsonObject);

    @POST("additional_farm/insert_farmer_new")
    Call<FarmerFarmInsertResponse> insertFarmerAndFarm(@Body JsonObject farmerAndFarmData);

    @POST("registry/getAllCompRegistry")
    @FormUrlEncoded
    Call<ResponseCompReg> getCompRegData(@Field("comp_id") String compId,
                                         @Field("person_id") String personId,
                                         @Field("user_id") String userId,
                                         @Field("token") String token);

    @POST("authentication/FarmerCompanyLogin")
    @FormUrlEncoded
    Call<Post> changeUserCreds(@Field("mob") String mob,
                               @Field("user_id") String userId,
                               @Field("token") String token,
                               @Field("comp_id") String compId);

    //    2300 in body for offline detected
    @POST("authentication/supervisor_login")
    @FormUrlEncoded
    Call<Post> superVisorLogin(@Field("username") String username,
                               @Field("password") String password,
                               @Field("gps") String gps,
                               @Field("ip") String ip,
                               @Field("app_version") String appVersion,
                               @Field("android_version") String android_version,
                               @Field("forced_online_mode") boolean forced_online_mode
    );
    @POST("authentication/supervisor_login")
    Call<Post> superVisorLogin(@Body JsonObject jsonObject    );


    @POST("authentication/mobileLogin")
    @FormUrlEncoded
    Call<Post> mobileLogin(@Field("mob") String mob,
                           @Field("password") String password,
                           @Field("gps") String gps,
                           @Field("ip") String ip,
                           @Field("app_version") String appVersion,
                           @Field("android_version") String android_version,
                           @Field("forced_online_mode") boolean forced_online_mode
    );

    @POST("authentication/farmer_login")
    @FormUrlEncoded
    Call<Post> farmerLogin(@Field("mob") String mobile,
                           @Field("gps") String gps,
                           @Field("app_version") String appVersion,
                           @Field("android_version") String android_version,
                           @Field("authFireBase") boolean authFireBase,
                           @Field("mac") String mac);

    @POST("android_farmer/farmer_inner_data")
    Call<FarmerInnerPagerResponse> getFarmInnerData(@Body JsonObject jsonObject);

    @POST("android/insert_location")
    Call<RecieveResponseGpsBack> getStatusMsgforUploadGpsInBack(@Body SendGpsArray sendGpsArray);

    @POST("android_voucher/delete_sv_exp")
    @FormUrlEncoded
    Call<ExpenseRemoveResponse> deleteExpense(@Field("sv_daily_exp_id") String expId,
                                              @Field("deleted_by") String deleted_by,
                                              @Field("user_id") String userId,
                                              @Field("token") String token);

    @POST("android_voucher/get_sv_exp")
    @FormUrlEncoded
    Call<ExpenseData> getExpenseData(@Field("comp_id") String comp_id,
                                     @Field("sv_id") String sv_id,
                                     @Field("user_id") String userId,
                                     @Field("token") String token);

    @Multipart
    @POST("android_voucher/add_sv_exp")
    Call<AddExpenseResponse> registerUser(@Part("exp_img") RequestBody image,
                                          @Part("comp_id") RequestBody comp_id,
                                          @Part("amount") RequestBody amount,
                                          @Part("sv_id") RequestBody sv_id,
                                          @Part("exp_date") RequestBody exp_date,
                                          @Part("comment") RequestBody comment,
                                          @Part("category_id") RequestBody category_id,
                                          @Part("user_id") RequestBody userId,
                                          @Part("token") RequestBody token);


    @POST("android_voucher/add_sv_exp")
    Call<AddExpenseResponse> registerUser2(@Body JsonObject object);

    @POST("android_farm/view_cluster_farms_paginate")
    Call<FetchFarmData> fetchFarmDatafncn(@Body FetchFarmSendData fetchFarmSendData);

    @POST("android_farm/get_farmcount_with_grades")
    Call<DashboardResponse> getClusterInsights(@Body SetCompIdClusterId setCompIdClusterId);

    @POST("android_farmer/farmer_dashboard")
    @FormUrlEncoded
    Call<DashboardResponse> getFarmerDashboardData(@Field("comp_id") String comp_id,
                                                   @Field("person_id") String person_id,
                                                   @Field("sesaon_num") String sesaon_num,
                                                   @Field("user_id") String userId,
                                                   @Field("token") String token);

    @POST("android_farm/get_all_farm_list")
    @FormUrlEncoded
    Call<MapResponse> getMapData(@Field("cluster_id") String cluster_id,
                                 @Field("user_id") String userId,
                                 @Field("token") String token);

    @POST("android_farm/get_all_farm_list")
    @FormUrlEncoded
    Call<MapResponse> getFarmerMapData(@Field("person_id") String person_id,
                                       @Field("user_id") String userId,
                                       @Field("token") String token);

    @POST("android_vr/get_harvest_farm")
    @FormUrlEncoded
    Call<FullDetailsResponse> getFarmAndHarvestData(@Field("farm_id") String farm_id,
                                                    @Field("comp_id") String comp_id,
                                                    @Field("user_id") String userId,
                                                    @Field("token") String token);

    @POST("android_farm/get_farm_gps")
    @FormUrlEncoded
    Call<GetGpsCoordinates> getGpsCoordinates(@Field("farm_id") String farm_id,
                                              @Field("comp_id") String comp_id,
                                              @Field("user_id") String userId,
                                              @Field("token") String token);

    @POST("android_farm/update_actual_flowering_date")
    Call<StatusMsgModel> getActualFloweringDateStatus(@Body HarvestAndFloweringSendData harvestAndFloweringSendData);

    @POST("android_farm/update_actual_harvest_date")
    Call<StatusMsgModel> getActualHarvestDateStatus(@Body HarvestAndFloweringSendData harvestAndFloweringSendData);

    @POST("android_calendar/timeline_new")
    @FormUrlEncoded
    Call<TimelineResponse> getCalendarData(@Field("comp_id") String comp_id,
                                           @Field("farm_id") String farm_id,
                                           @Field("user_id") String userId,
                                           @Field("token") String token);

    @POST("android_vr/get_crop_density_sample_data")
    Call<SampleGerminationStatusNdData> getSampleGermiData(@Body SendGerminationSpacingData sendGerminationSpacingData);

    @POST("android_farm/insert_crop_density_samples")
    Call<StatusMsgModel> getStatusMsgForGerminationAndSpacing(@Body SendGerminationSpacingData sendGerminationSpacingData);

    @POST("android_farm/update_farm")
    Call<StatusMsgModel> getMsgStatusForFarmDetailsUpdate(@Body FarmDetailsUpdateSendData farmDetailsUpdateSendData);

    @POST("android_farm/farm_gps_submit")
    Call<StatusMsgModel> getMsgStatusForVerifyFarm(@Body VerifySendData verifySendData);

    @POST("android_farm/update_standing_area")
    @FormUrlEncoded
    Call<UpdateStandingResponse> updateStandingAcres(@Field("farm_id") String farm_id,
                                                     @Field("standing_acres") String standing_acres,
                                                     @Field("user_id") String userId,
                                                     @Field("token") String token);

    @POST("android_farm/offline_timeline")
    Call<FetchFarmData> fetchFarmDatafncnold(@Body FetchFarmSendData fetchFarmSendData);

    @POST("android_farm/send_offline_timeline")
    Call<OfflineResponse> getOfflineData(@Body FarmIdArray farmIdArray);

    @POST("android_harvest/select_data_of_a_plan")
    Call<GpsMainDataForSingleHarvest> getGpsDataForSingleHarvest(@Body SendDataForMapSingleHarvestPlan sendDataForMapSingleHarvestPlan);

    @POST("android_harvest/select_harvested_data_for_planning")
    @FormUrlEncoded
    Call<HarvestCollectionMainData> getHarvestDataForCollection(@Field("cluster_id") String clusterId,
                                                                @Field("user_id") String userId,
                                                                @Field("token") String token);

    @POST("android_harvest/insert_planned_harvested_data")
    Call<HarvestSubmitData> getHarvestSubmitPlannedData(@Body HarvestSendPlannedData harvestSendPlannedData);

    @POST("android_harvest/select_all_plans")
    @FormUrlEncoded
    Call<ShowHarvestPlanMain> getShowHarvestPlannedData(@Field("svid") String personId,
                                                        @Field("user_id") String userId,
                                                        @Field("token") String token);

    @POST("android/fetch_profile")
    @FormUrlEncoded
    Call<ProfileReciveMainData> getProfileDataforSupervisor(@Field("person_id") String personId,
                                                            @Field("comp_id") String compId,
                                                            @Field("user_id") String userId,
                                                            @Field("token") String token);

    @POST("android/fetch_profile")
    @FormUrlEncoded
    Call<ResponseBody> getProfileDataforSupervisor2(@Field("person_id") String personId,
                                                    @Field("comp_id") String compId,
                                                    @Field("user_id") String userId,
                                                    @Field("token") String token);


    @POST("android_app/reset_password")
    Call<StatusMsgModel> getStatusMsgForResetPassword(@Body ResetPassSendData resetPassSendData);

    @POST("android/update_profile")
    Call<StatusMsgModel> getAddNewProfile(@Body AddProfileSendData AddprofileSendData);

    @POST("android_app/update_sv_img")
    Call<ImageUpdateResponse> updateUserProfile(@Body ImageBody imageBody);

    @POST("farm/farmVisitAgriInput")
    Call<StatusMsgModel> getVisitMsgStatusNew(@Body AddVisitSendDataNew addVisitSendData);

    @POST("android_calendar/input_data_farmer_reply")
    Call<StatusMsgModel> setTimelineActivity(@Body SendTimelineActivityData activityData);

    @POST("farm/farmAgriInputsInsert")
    Call<StatusMsgModel> setTimelineActivityNew(@Body SendTimelineActivityDataNew activityData);


    @POST("android_calendar/get_units")
    @FormUrlEncoded
    Call<ActivityUnitResponse> getActivityUnits(@Field("user_id") String userId,
                                                @Field("token") String token);

    @POST("android_calendar/get_chemical")
    @FormUrlEncoded
    Call<ActivityChemicalResponse> getActivityChemicals(@Field("comp_id") String comp_id,
                                                        @Field("user_id") String userId,
                                                        @Field("token") String token);

    @POST("android_farm/update_pld_acres")
    Call<StatusMsgModel> getStatusMsgForPldAcres(@Body SendPldData sendPldData);

    @POST("android_farm/add_pld_new")
    Call<StatusMsgModel> getStatusMsgForPldArea(@Body SendPldData sendPldData);

    @POST("android_harvest/select_inserted_harvest_data")
    @FormUrlEncoded
    Call<ViewHarvestDetails> getHarvestDetailStatus(@Field("farm_id") String farmId,
                                                    @Field("user_id") String userId,
                                                    @Field("token") String token);

    @POST("android_harvest/insert_harvest_data")
    Call<StatusMsgModel> getStatusMsgModelForHarvestBags(@Body SendHarvestData sendHarvestData);

    @POST("android_farmer/get_farmer_farms")
    Call<FetchFarmerFarmResponse> getFarmerFarms(@Body JsonObject jsonObject);

    @POST("android_farm/add_input_cost")
    @FormUrlEncoded
    Call<CostSubmitResponse> insertCost(@Field("expense") String expense,
                                        @Field("comp_id") String compId,
                                        @Field("farm_id") String farmId,
                                        @Field("particular") String comment,
                                        @Field("activity_type_id") String activityTypeId,
                                        @Field("expense_date") String expenseDate,
                                        @Field("added_by") String addedBy,
                                        @Field("user_id") String userId,
                                        @Field("token") String token);

    @POST("android_farmer/get_farmer_of_cluster")
    @FormUrlEncoded
    Call<FetchFarmerResponse> getFarmerList(@Field("comp_id") String comp_id,
                                            @Field("cluster_id") String clusterId,
                                            @Field("user_id") String userId,
                                            @Field("token") String token);

    @POST("additional_farm/addMoreFarmOfFarmer")
    Call<StatusMsgModel> insertExistingFarmerFarm(@Body FarmerAndFarmData farmData);

    @POST("additional_farm/addMoreFarmOfFarmer")
    Call<StatusMsgModel> insertExistingFarmerFarm(@Body JsonObject farmData);

    @POST("android_farm/get_dropdown_for_input")
    @FormUrlEncoded
    Call<CostDropdownResponse> getInputDropDownData(@Field("comp_id") String compId,
                                                    @Field("user_id") String userId,
                                                    @Field("token") String token);

    @POST("android_farm/get_resources_and_input")
    @FormUrlEncoded
    Call<InputCostResponse> getInputCostList(@Field("farm_id") String farmId,
                                             @Field("user_id") String userId,
                                             @Field("token") String token);

    @POST("android_farm/add_input_and_resource")
    Call<CostSubmitResponse> insertCostAndResourceData(@Body CostAndResourceSubmitData data);

    @POST("notification/get_user_notification_list")
    @FormUrlEncoded
    Call<ShowNotificationResponse> getListOfNotifications(@Field("person_id") String personId,
                                                          @Field("user_id") String userId,
                                                          @Field("token") String token);

    //This api response not handled with token
    @POST("notification/update_notification_read")
    @FormUrlEncoded
    Call<ResponseBody> setNotificationAsRead(@Field("noti_sch_id") String noti_sch_id,
                                             @Field("user_id") String userId,
                                             @Field("token") String token);

    @POST("android_farm/offline_calendar_activity")
    Call<CalendarActResponse> getDoneCalActivities(@Body FarmIdArray farmIdArray);

    @POST("android_farm/update_sowing_date")
    Call<StatusMsgModel> getSowingDateStatus(@Body HarvestAndFloweringSendData harvestAndFloweringSendData);

    @POST("android_farm/get_card_parameter")
    @FormUrlEncoded
    Call<HealthCardDDResponse> getSoilHealthCardDropDown(@Field("user_id") String userId,
                                                         @Field("token") String token,
                                                         @Field("comp_id") String compId);

    @POST("android_farm/add_soil_card")
    Call<StatusMsgModel> uploadHealthCardData(@Body SendHealthCardData sendHealthCardData);


    @POST("android_farm/get_all_soil_card_of_farm")
    @FormUrlEncoded
    Call<HealthCardResponse> getPreviousHealthCard(@Field("user_id") String userId,
                                                   @Field("token") String token,
                                                   @Field("farm_id") String farmId);


    @POST("farm/update_seed_issue")
    Call<StatusMsgModel> addSeedQty(@Body JsonObject object);

    @POST("cluster/get_clusters")
    @FormUrlEncoded
    Call<ClusterResponse> getClusters(@Field("user_id") String userId,
                                      @Field("token") String token,
                                      @Field("comp_id") String compId);

    /*@POST("admin_setting/satallite_farm_data")
    @FormUrlEncoded
    Call<SatsureResponse> getSatsureData(@Field("farm_id") String farmId);*/

    /*@POST("satellite/get_particular_farm_satallite_data")
    @FormUrlEncoded
    Call<SatsureResponse> getSatsureData(@Field("farm_id") String farmId);*/

    @POST("satellite/get_satData_by_farmId")
    @FormUrlEncoded
    Call<SatsureResponse> getSatsureData(@Field("farm_id") String farmId, @Field("user_id") String userId);

    @POST("satellite/getImagesByProducts")
    @FormUrlEncoded
    Call<SatsureResponse> getSatsureData(@Field("farm_id") String farmId,
                                         @Field("product_id") String productId,
                                         @Field("user_id") String userId);

    /* @POST("admin_setting/productData")
     @FormUrlEncoded
     Call<ProductResponse> getSatsureProducts(@Field("farm_id") String farmId,@Field("user_id") String userId);
 */
    @POST("android_farm/pld_reason_list")
    @FormUrlEncoded
    Call<PldReasonResponse> getPldReasonList(@Field("user_id") String userId,
                                             @Field("token") String token,
                                             @Field("comp_id") String compId);

    @POST("android_farm/get_pld")
    @FormUrlEncoded
    Call<PldResponse> getPldReasonListOfFarm(@Field("user_id") String userId,
                                             @Field("token") String token,
                                             @Field("farm_id") String farmId);


    @POST("android/serverTime")
    @FormUrlEncoded
    Call<StatusMsgModel> getCurrentTime(@Field("user_id") String userId);

    /*@POST("satellite/productData")
    @FormUrlEncoded
    Call<ResponseBody> getProducts(@Field("user_id") String userId);*/

    @POST("Unauth/productData")
    @FormUrlEncoded
    Call<ResponseBody> getProducts(@Field("user_id") String userId,
                                   @Field("token") String token);

    @POST("android_harvest/pickedup_status_update")
    Call<StatusMsgModel> updateStatusOfHarvestCollection(@Body StatusChangeCollectionData data);

    @POST("android_harvest/fetch_harvest_sale")
    @FormUrlEncoded
    Call<SellResponse> getSellData(@Field("user_id") String userId,
                                   @Field("token") String token,
                                   @Field("farm_id") String farmId);

    @POST("android_harvest/update_harvest_sale_data")
    Call<StatusMsgModel> updateProduceSell(@Body JsonObject jsonObject);

    @POST("android_harvest/insert_harvest_sale_data")
    Call<StatusMsgModel> addSellRecord(@Body JsonObject jsonObject);

    @POST("calendar/fetch_all_calendarmaster_data")
    @FormUrlEncoded
    Call<CalendarFetchResponse> getCropCycles(@Field("user_id") String userId,
                                              @Field("token") String token,
                                              @Field("comp_id") String compId);

    @POST("calendar/get_copyof_farm_calendar")
    Call<StatusMsgModel> assignCalendar(@Body JsonObject jsonObject);

    @POST("calendar/fetch_FarmCalendarMaster_ById")
    @FormUrlEncoded
    Call<SingleCalendarResponse> getParticularCalendar(@Field("user_id") String userId,
                                                       @Field("token") String token,
                                                       @Field("farm_id") String farmId,
                                                       @Field("comp_id") String compId);

    @POST("android_calendar/get_weather_data")
    @FormUrlEncoded
    Call<WeatherMainRes> getWeatherData(@Field("user_id") String userId,
                                        @Field("token") String token,
                                        @Field("long") String longg,
                                        @Field("lat") String lat,
                                        @Field("farm_id") String farmId);

    @POST("user/get_query_subjects_list")
    @FormUrlEncoded
    Call<QueryParamResponse> getQueryParams(@Field("user_id") String userId,
                                            @Field("token") String token);

    @POST("user/add_user_query")
    @FormUrlEncoded
    Call<StatusMsgModel> addUserQuery(@Field("msg") String query,
                                      @Field("user_id") String userId,
                                      @Field("token") String token,
                                      @Field("id") String paramId);


    @POST("authentication/getCompanybyMobile")
    @FormUrlEncoded
    Call<CompanyResponse> getCompanyList(@Field("mob") String mob,
                                         @Field("user_id") String userId,
                                         @Field("token") String token);

    @POST("android_farm/farmsLocations")
    Call<FarmsCoordinatesResponse> getAllCoordinatesOfFarms(@Body FarmIdArray farmIdArray);

//    @POST("android_offline/offline_insert_farmer_new")
//    Call<OfflineAddFarmResponse> insertFarmerAndFarmOffline(@Body JsonObject farmerAndFarmData);

    @POST("additional_farm/addMoreFarmOfFarmer")
    Call<FarmerFarmInsertResponse> insertExistingFarmerFarmOfflineNew(@Body JsonObject farmData);

//    @POST("android_offline/offline_addMoreFarmOfFarmer")
//    Call<OfflineAddFarmResponse> insertExistingFarmerFarmOffline(@Body JsonObject farmData);

    @POST("android_offline/updateOfflineFarmData")
    Call<StatusMsgModel> uploadOfflineFarm(@Body SendOfflineFarm sendOfflineFarm);

//    @POST("android_offline/offline_insert_data_resource_multiple")
//    Call<StatusMsgModel> uploadOfflineResourceData(@Body SendOfflineResource sendOfflineResource);

//    @POST("android_offline/offline_insert_input_cost")
//    Call<StatusMsgModel> uploadOfflineInputCost(@Body SendOfflineInputCost inputCostData);

//    @POST("android_offline/offline_calendar_activity_insert")
//    Call<StatusMsgModel> uploadOfflineCalActivities(@Body SendTimelineActivityDataNew sendTimelineActivityDataNew);

//    @POST("android_offline/offline_insert_visit_report")
//    Call<StatusMsgModel> uploadOfflineVisit(@Body AddVisitSendDataNew addVisitSendDataNew);

//    @POST("android_offline/offline_insert_harvest_data")
//    Call<StatusMsgModel> uploadOfflineHarvest(@Body ViewHarvestDetails viewHarvestDetails);

//    @POST("android_offline/add_pld_new")
//    Call<StatusMsgModel> getStatusMsgForPldAreaO(@Body SendPldData sendPldData);

    @POST("android_farm/farmGpsFilterByFarm")
    Call<SelectionResponse> getFarmerFarmNonGps(@Body FarmerData farmerData);

    @POST("farm/freshFarmVeitting")
    Call<VettingFarmResponse> getVettingPending(@Body FetchFarmSendData fetchFarmSendData);

    @POST("farm/rejectedFarmVetting")
    Call<VettingFarmResponse> getVettingRejected(@Body FetchFarmSendData fetchFarmSendData);

    @POST("farm/requiredDEVetting")
    Call<VettingFarmResponse> getVettingDataEntry(@Body FetchFarmSendData fetchFarmSendData);

    @POST("farm/selectedFarmVetting")
    Call<VettingFarmResponse> getVettingSelected(@Body FetchFarmSendData fetchFarmSendData);

    @POST("farm/allVettingFarms")
    Call<FarmResponseNew> getAllVetting(@Body FetchFarmSendData fetchFarmSendData);

    @POST("farm/farmFilters")
    Call<FarmResponseNew> getFarmFilters(@Body JsonObject fetchFarmSendData);

    @POST("farm/searchFarmForVetting")
    Call<VettingFarmResponse> getVettingSearch(@Body VettingSearchBody fetchFarmSendData);

    @POST("company/getCompanyParameters")
    Call<DDResponseNew> getNewParameters(@Body ParamData paramData);

    @POST("farm/farmAddInfo")
    Call<CropFormResponse> getFarmCropForm(@Body ParamData paramData);

    @POST("registry/getCountries")
    @FormUrlEncoded
    Call<CountryResponse> getAllCountries(@Field("user_id") String userId,
                                          @Field("token") String token);

    @POST("registry/getstates")
    @FormUrlEncoded
    Call<StateResponse> getAllStates(@Field("user_id") String userId,
                                     @Field("token") String token, @Field("country_id") String countryId);

    @POST("registry/getCities")
    @FormUrlEncoded
    Call<CityResponse> getAllCity(@Field("user_id") String userId,
                                  @Field("token") String token,
                                  @Field("country_id") String countryId,
                                  @Field("state_id") String stateId);

    @POST("unit/updateUserAreaUnit")
    Call<StatusMsgModel> changeAreaUnit(@Body ParamData paramData);

    @POST("farm/updateVettingStatusFarm")
    @FormUrlEncoded
    Call<StatusMsgModel> updateVettingStatus(@Field("user_id") String userId,
                                             @Field("token") String token,
                                             @Field("comp_id") String compId,
                                             @Field("farm_id") String farmID,
                                             @Field("isSelected") String isSelected);

    @POST("android_farm/farm_gps2_submit")
    Call<StatusMsgModel> uploadGps(@Body FarmLocationData paramData);

    @POST("android_farm/additionalGpsData")
    Call<StatusMsgModel> uploadWayPoints(@Body SendWayPointData paramData);

    @POST("iot_soilsens/insertFarmSoilsensData")
    Call<StatusMsgModel> addSoilSenseData(@Body SendSoilData paramData);

    @POST("farm/getSoilsenseDataTimeline")
    @FormUrlEncoded
    Call<SoilSenseResponse> getSoilSensData(@Field("comp_id") String comp_id,
                                            @Field("farm_id") String farm_id,
                                            @Field("user_id") String userId,
                                            @Field("token") String token);

    @POST("android_farm/searchNearByFarms")
    @FormUrlEncoded
    Call<NearFarmResponse> getNearFarms(@Field("comp_id") String comp_id,
                                        @Field("farm_id") String farm_id,
                                        @Field("user_id") String userId,
                                        @Field("token") String token,
                                        @Field("lat") String lat,
                                        @Field("long") String lon,
                                        @Field("cluster_id") String clusterId);

    @POST("android_farm/insertOmitance")
    Call<StatusMsgModel> submitOmitanceArea(@Body SendOmtanceArea paramData);

    @POST("android_farm/fetchAdditionalGps")
    Call<OmitanceShowResponse> getOmitanceArea(@Body SendOmtanceArea paramData);

    @POST("android_farm/fetchAdditionalGps")
    Call<WayPointFetchResponse> getWayPoints(@Body SendOmtanceArea paramData);

    @POST("android_farm/fetchAdditionalGps")
    Call<ResponseBody> getWayPoints2(@Body SendOmtanceArea paramData);

    @POST("calendar/select_farm_activities_and_instruction")
    @FormUrlEncoded
    Call<ResponseBody> getAgriInputData(@Field("farm_cal_activity_id") String farm_cal_activity_id,
                                        @Field("user_id") String userId,
                                        @Field("token") String token);

    @POST("calendar/select_farm_activities_and_instruction")
    @FormUrlEncoded
    Call<AgriInputResponse> getAgriInputData2(@Field("farm_cal_activity_id") String farm_cal_activity_id,
                                              @Field("user_id") String userId,
                                              @Field("token") String token);

    @POST("company/getCompanyParameters")
    @FormUrlEncoded
    Call<CompParamResponse> getCompParam(@Field("comp_id") String compId,
                                         @Field("type") String type,
                                         @Field("user_id") String userId,
                                         @Field("token") String token);

    @POST("farm/agriInputdetailsFetch")
    @FormUrlEncoded
    Call<CompAgriResponse> getCompAgriInputs(@Field("comp_id") String compId,
                                             @Field("user_id") String userId,
                                             @Field("token") String token);

    @POST("farm/allVettingFarmsForSup")
    Call<OtherVettingFarmResponse> getOtherVetting(@Body FetchFarmSendData fetchFarmSendData);

    @POST("user/view_supervisors")
    @FormUrlEncoded
    Call<SupervisorListResponse> getCompSupervisors(@Field("cluster_id") String clusterId,
                                                    @Field("comp_id") String compId,
                                                    @Field("user_id") String userId,
                                                    @Field("token") String token);

    @POST("farm/fetchFarmCalendarData")
    @FormUrlEncoded
    Call<DoneActivityResponseNew> getCalendarActData(@Field("farm_calendar_activity_id") String farm_calendar_activity_id,
                                                     @Field("user_id") String userId,
                                                     @Field("token") String token);


    @POST("farm/fetchFarmVIsitData")
    @FormUrlEncoded
    Call<FetchVisitResponse> getVisitDetails(@Field("vr_id") String farm_calendar_activity_id,
                                             @Field("user_id") String userId,
                                             @Field("token") String token);


    @POST("additional_farm/selectFarmDetails")
    Call<FarmFullDetails> getFullFarmDetails(@Body JsonObject jsonObject);

    @POST("user/fetchPersonByMob")
    Call<PersonFullResponse> getDeialsByMob(@Body JsonObject jsonObject);

    @POST("user/fetchPersonByMob")
    Call<ResponseBody> getDeialsByMob2(@Body JsonObject jsonObject);

    @POST("additional_farm/updateFarmerWithFarmDetails")
    Call<UpdatRsponse> updateFarmAndFarmer(@Body JsonObject farmerAndFarmData);

    @POST("additional_farm/updateAddressProof")
    Call<StatusMsgModel> updateAddressProf(@Body JsonObject farmerAndFarmData);

    @POST("additional_farm/updateIDProof")
    Call<StatusMsgModel> updateIdProff(@Body JsonObject farmerAndFarmData);

    @POST("farm/farmCounts")
    @FormUrlEncoded
    Call<FarmCountResponse> getVettingFarmCounts(@Field("cluster_id") String clusterId,
                                                 @Field("comp_id") String compId,
                                                 @Field("user_id") String userId,
                                                 @Field("token") String token);

    @POST("farm/fetchChakLeader")
    Call<FetchChakResponse> getChakLeader(@Body JsonObject jsonObject);


    @POST("farm/assignChakLeader")
    Call<StatusMsgModel> setChakLeader(@Body JsonObject jsonObject);

    @POST("farm/farmGeoAnalytics")
    Call<GeoCardsResponse> getGeoFenceCount(@Body JsonObject jsonObject);

    @POST("farm/getVillagesOfFarms")
    Call<ClusterVillageResponse> getClusterVillages(@Body JsonObject jsonObject);


    @POST("android_farm/fetchChak")
    Call<VillageChakResponse> getChaksOfVillage(@Body JsonObject jsonObject);


    @POST("android_offline/goOffline")
    @FormUrlEncoded
    Call<StatusMsgModel> setDeviceId(@Field("off_device_id") String deviceId,
                                     @Field("user_id") String userId,
                                     @Field("token") String token);


    @POST("android_offline/goOnline")
    @FormUrlEncoded
    Call<StatusMsgModel> wipeDeviceId(
            @Field("user_id") String userId,
            @Field("token") String token);


    @POST("additional_farm/updateAllFarmDetails")
    Call<StatusMsgModel> updateFarmDetails(@Body JsonObject farmData);

    @POST("gps/insertFarmGps")
    Call<StatusMsgModel> submitGps(@Body JsonObject farmData);

    @POST("gps/fetchFarmGps")
    Call<GeoJsonResponse> getFarmGeoJson(@Body JsonObject farmData);

    @POST("season/fetch_seasons")
    Call<SeasonResponse> getSeasons(@Body FetchFarmSendData farmData);

    @POST("Authentication/forgot_Password")
    Call<StatusMsgModel> forgotPassword(@Body JsonObject jsonObject);

    @POST("android_farm/fetchVisitReport")
    Call<ResponseBody> getAllVisitsOfFarm(@Body JsonObject jsonObject);


    @POST("android_farm/fetchVisitReport")
    Call<FarmVisitResponse> getAllVisitsOfFarmModel(@Body JsonObject jsonObject);

    @POST("farm/fetchSupervisorTask")
    Call<TaskResponse> getSvTasks(@Body JsonObject jsonObject);

    @POST("farm/updateSupervisorTask")
    Call<StatusMsgModel> completeTask(@Body JsonObject jsonObject);

    @POST("farm/addedByFarms")
    Call<SvFarmResponse> getSvFarms(@Body JsonObject jsonObject);


    @POST("android_voucher/get_sv_exp")
    Call<ExpenseData> getExpenseData(@Body JsonObject jsonObject);

    @POST("utils/qrGen")
    @FormUrlEncoded
    Call<QRResponse> getQrImage(@Field("hm_id") String id,
                                @Field("type") String type);

    @POST("utils/qrGen")
    @FormUrlEncoded
    Call<ResponseBody> getQrImage1(@Field("hm_id") String id,
                                   @Field("farm_id") String farmId,
                                   @Field("type") String type);

    @POST("authentication/farmer_login")
    Call<Post> farmerLogin(@Body JsonObject jsonObject);

    @POST("authentication/getCompanybyMobile")
    @FormUrlEncoded
    Call<CompanyResponse> getCompanyList(@Field("mob") String mob,
                                         @Field("user_id") String userId,
                                         @Field("token") String token,
                                         @Field("comp_id") String compId);

    @POST("authentication/mobileLogin")
    @FormUrlEncoded
    Call<Post> mobileLogin(@Field("mob") String mob,
                           @Field("password") String password,
                           @Field("gps") String gps,
                           @Field("ip") String ip,
                           @Field("app_version") String appVersion,
                           @Field("android_version") String android_version,
                           @Field("forced_online_mode") boolean forced_online_mode,
                           @Field("comp_id") String compId
    );

    @POST("authentication/mobileLogin")
    Call<Post> mobileLogin(@Body JsonObject farmData);

    @POST("registration/addIndependentSupervisor")
    Call<StatusMsgModel> registerSv(@Body JsonObject farmData);

    @POST("additional_farm/generateFarmerOTP")
    Call<StatusMsgModel> callPDBMAuth(@Body JsonObject farmData);


    @POST("utils/qrGen")
    @FormUrlEncoded
    Call<QRResponse> getFarmQr(@Field("farm_id") String id,
                               @Field("type") String farm,
                               @Field("user_id") String userId,
                               @Field("token") String token);


   /* @POST("utils/qrFarmGen")
    @FormUrlEncoded
    Call<QRResponse> getFarmQr(@Field("farm_id") String id);
*/

    @POST("farm/encFarmDetails")
    Call<EncFarmResponse> getFarmIdFromEncFarmId(@Body JsonObject jsonObject);



    @POST("farm/fetchAgriInput")
    Call<FarmAgriInputResponse> getFarmAgriInputs(@Body JsonObject jsonObject);


    @POST("farm/fetchAgriInput")
    Call<ResponseBody> getFarmAgriInputs2(@Body JsonObject jsonObject);

    @POST("cases/createCases")
    Call<StatusMsgModel> addMessage(@Body JsonObject jsonObject);


    @POST("calendar/get_activity_type")
    Call<ActivityTypeResponse> getActivityTypes(@Body JsonObject farmData);

    @POST("calendar/insert_calendar_details")
    Call<StatusMsgModel> createCalendar(@Body JsonObject farmData);


    @Multipart
    @POST("cases/createCases")
    Call<InsertCaseResponse> addMessageWithFiles(@Part List<MultipartBody.Part> files,
                                                 @Part("user_id")RequestBody userId,
                                                 @Part("comp_id")RequestBody comp_id,
                                                 @Part("token")RequestBody token,
                                                 @Part("person_id")RequestBody person_id,
                                                 @Part("sender_id")RequestBody sender_id,
                                                 @Part("message")RequestBody message,
                                                 @Part("subject")RequestBody subject,
                                                 @Part("receiver_id")RequestBody receiver_id,
                                                 @Part("status")RequestBody status,
                                                 @Part("is_sent")RequestBody is_sent,
                                                 @Part("is_read")RequestBody is_read,
                                                 @Part("case_id")RequestBody case_id);

    @POST("cases/viewCases")
    Call<CaseListResponse> getCases(@Body JsonObject jsonObject);

    @POST("cases/updateCaseStatus")
    Call<StatusMsgModel> changeStatusOfCase(@Body JsonObject jsonObject);

    @POST("farm/get_farm_gps")
    Call< com.i9930.croptrails.Maps.ShowArea.Model.near.NearFarmResponse> getNearFarms(@Body JsonObject jsonObject);

    @POST("noticeBoard/fetchPost")
    Call<NoticeResponse> getNoticeBoard(@Body JsonObject jsonObject);

    @POST("gps/fetchFarmCords")
    Call<SvMapFarmResponse> getSvFarmsCoordinates(@Body JsonObject jsonObject);

    @POST("noticeBoard/viewPost")
    Call<StatusMsgModel> increaseViewCount(@Body JsonObject jsonObject);

    @POST("noticeBoard/fetchSinglePost")
    Call<SinglePostResponse> getPostDetails(@Body JsonObject jsonObject);


    @POST("user/ifscDetails")
    @FormUrlEncoded
    Call<IfscResponse> checkIfscCode(@Field("ifsc")String ifsc);

    @POST("farm/FarmAgriInputInsert")
    Call<StatusMsgModel> addAgriInputData(@Body JsonObject jsonObject);


    @POST("additional_farm/updateFarmerNew")
    Call<UpdatRsponse> updateFarmerDetails(@Body JsonObject farmerAndFarmData);

    @POST("additional_farm/updateFarmDetails")
    Call<UpdatRsponse> updateFarmDetailsNew(@Body JsonObject farmerAndFarmData);


    @POST("additional_farm/updateAssetInfo")
    Call<UpdatRsponse> updateFarmAssets(@Body JsonObject farmerAndFarmData);


    @POST("additional_farm/updateCropInfo")
    Call<UpdatRsponse> updateFarmCrop(@Body JsonObject farmerAndFarmData);
    @POST("additional_farm/registerFreshFarmer")
    Call<StatusMsgModel> registerFarmer(@Body JsonObject farmData);

}
