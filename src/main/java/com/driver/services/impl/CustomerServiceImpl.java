package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer = customerRepository2.findById(customerId).get();
		customerRepository2.delete(customer);
	}

	@Override
		public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
			//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
			//Avoid using SQL query

			List<Driver> drivers = driverRepository2.findAll();
			Driver bookedriver = null;
			for(Driver driver : drivers){
				if(driver.getCab().isAvailable()){
					if(bookedriver == null || bookedriver.getDriverId() > driver.getDriverId()){
						bookedriver = driver;
					}
				}
			}
			if(bookedriver == null)
				throw new Exception("No cab available!");

			TripBooking tripBooking = new TripBooking();
			Customer customer = customerRepository2.findById(customerId).get();
			tripBooking.setCustomer(customer);
			tripBooking.setFromLocation(fromLocation);
			tripBooking.setToLocation(toLocation);
			tripBooking.setDistanceInKm(distanceInKm);
			tripBooking.setBill(bookedriver.getCab().getPerKmRate() * distanceInKm);
			tripBooking.setStatus(TripStatus.CONFIRMED);
			tripBooking.setDriver(bookedriver);

			bookedriver.getTripBookingList().add(tripBooking);
			customer.getTripBookingList().add(tripBooking);

			driverRepository2.save(bookedriver);

			customerRepository2.save(customer);
			return tripBooking;


		}

		@Override
		public void cancelTrip(Integer tripId){
			TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
			tripBooking.setStatus(TripStatus.CANCELED);
			tripBooking.setBill(0);
			tripBooking.getDriver().getCab().setAvailable(true);
			tripBookingRepository2.save(tripBooking);


		}

		@Override
		public void completeTrip(Integer tripId){
			//Complete the trip having given trip Id and update TripBooking attributes accordingly
			TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
			tripBooking.setStatus(TripStatus.COMPLETED);
			tripBooking.getDriver().getCab().setAvailable(true);
			tripBookingRepository2.save(tripBooking);

		}
		/*
		Customer customer;
		customer = customerRepository2.findById(customerId).get();

		try {
			if(!customerRepository2.existsById(customerId)) throw new Exception();
		}
		catch(Exception e){
			throw new Exception ("No value present");
		}


		// create trip booking object
		TripBooking tripBooking = new TripBooking();
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setCustomer(customer);


		// check driver availability and find driver with minimum driver id
		Driver driver  = null;
		List<Driver> driverList = driverRepository2.findAll();
		if(driverList.isEmpty()){
			throw new Exception("No value present");
		}
		if(driverList.size() == 0){
			throw new Exception();
		}
		int minId = Integer.MAX_VALUE;
		for(Driver driverTemp : driverList){
			if(driverTemp.getCab().isAvailable() && minId>driverTemp.getDriverId()){
				minId = driverTemp.getDriverId();
				driver = driverTemp;
			}
		}
		if(driver == null){
			//if no driver available
			//tripBooking.setTripStatus(TripStatus.CANCELED);
			//tripBookingRepository2.save(tripBooking);
			throw new Exception("No value present");
		}

		// book the trip
		tripBooking.setDriver(driver);
		int bill = driver.getCab().getPerKmRate() * distanceInKm;
		tripBooking.setBill(bill);
		tripBooking.setTripStatus(TripStatus.CONFIRMED);
		tripBookingRepository2.save(tripBooking);

		// set driver to not available and add trip booking to the list
		driver.getCab().setAvailable(false);
		//driver.getTripBookingList().add(tripBooking);
		driverRepository2.save(driver);

		//	add trip booking to the list in customer
		//customer.getTripBookingList().add(tripBooking);

		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setTripStatus(TripStatus.CANCELED);

		Customer customer = tripBooking.getCustomer();
		List<TripBooking> tripBookingList =customer.getTripBookingList();
		tripBookingList.add(tripBooking);
		customer.setTripBookingList(tripBookingList);
		customerRepository2.save(customer);

		// update the driver to available for next ride
		Driver driver = tripBooking.getDriver();
		List<TripBooking> tripBookingList2 = driver.getTripBookingList();
		tripBookingList2.add(tripBooking);
		driver.setTripBookingList(tripBookingList2);

		driver.getCab().setAvailable(true);
		driverRepository2.save(driver);

	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setTripStatus(TripStatus.COMPLETED);
		tripBooking.getCustomer().getTripBookingList().add(tripBooking);
		tripBooking.getDriver().getTripBookingList().add(tripBooking);
		tripBookingRepository2.save(tripBooking);

		// update the driver to available for next ride
		Driver driver = tripBooking.getDriver();
		driver.getCab().setAvailable(true);
		driverRepository2.save(driver);

		 */
}
