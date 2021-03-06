package com.baidu.mapapi.search.route;

public interface OnGetRoutePlanResultListener {
    void onGetBikingRouteResult(BikingRouteResult bikingRouteResult);

    void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult);

    void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult);

    void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult);

    void onGetTransitRouteResult(TransitRouteResult transitRouteResult);

    void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult);
}
