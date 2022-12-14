package com.example.danocoffee.controller;

import com.example.danocoffee.data.*;
import com.example.danocoffee.repository.ManagerRepository;
import com.example.danocoffee.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {
	@Autowired
	private ManagerRepository managerRepository;
	@Autowired
	private AdminService adminService;

	@GetMapping("/manager")
	public List<Manager> Management() {
		return managerRepository.findAll();
	}

	@PostMapping("/addCrew")
	public Result addCrew(@RequestBody Manager manager) {
		Optional<Manager> findCrew = managerRepository.findById(manager.getMnId());
		if (findCrew.isPresent()) {
			managerRepository.save(manager); // 없는 경우 추가 있는 경우 변경 -> save함수
		} else {
			managerRepository.save(manager);
		}
		return new Result("ok");
	}

	@PutMapping("/updateMnName") // 관리자 이름 수정 (기존 updateMnId -> updateMnName)
	public Result updatemnId(@RequestBody Manager manager) throws Exception {
		System.out.println("asdf");
		Manager updatemnName = adminService.findManagerMnId(manager.getMnId());
		// (기존 updatemnId -> updatemnName 변경 )
		// (기존 findbyId(manager.getMnNumber()) -> findManagerMnId(manager.getMnId()) 변경)
		System.out.println(manager.getMnId());
		System.out.println(manager.getNewMnName());

		if (updatemnName == null) { // 관리자 아이디 실패
			return new Result("no");
		} else {
			updatemnName.setMnName(manager.getNewMnName());
			// (기존 setMnId -> setMnName 변경) , (기존 getNewMnId() -> getNewMnName() 변경)
			adminService.save(updatemnName);

			return new Result("ok"); // 관리자 이름 수정 성공
		}
	}

	@DeleteMapping("/deleteManager") // 관리자 삭제
	public Result deleteManager(@RequestBody Manager manager) throws Exception {
		System.out.println("asdf");
		System.out.println(manager.getMnId());
		Manager deleteManager = adminService.findManagerId(manager.getMnId());
		System.out.println(deleteManager);
		System.out.println(manager.getMnId());
		String a = manager.getMnId().toString();
		System.out.println("a : " + a );
		if (deleteManager == null) {
			return new Result("no"); // 삭제 실패
		} else {
			managerRepository.deleteByMnId(a); // 없는 경우 추가 있는 경우 변경 -> save함수
			return new Result("ok");
		}
	}

	@PostMapping(value = "/addMenu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 멀티파트 폼데이터 쓰겠다고 빈에 알려줌.
	public String addMenu(@RequestParam("mName") String mName, @RequestParam("mPrice") int mPrice,
			@RequestParam("mInven") boolean mInven, @RequestParam("mImg") MultipartFile mImg,
			@RequestParam("cId") int cId, HttpServletRequest request) throws IOException {
		System.out.println("image" + mImg);
		String path = request.getSession().getServletContext().getRealPath("\\"); // 파일 저장할 경로 가져옴.
		// 파일 업로드 하는 부분
		String fileName = UUID.randomUUID() + mImg.getOriginalFilename();
		String pathName = path + "\\static\\images\\" + fileName; // 저장된 파일 이름과 경로 합친것
		// UUID : 랜덤값으로 이름 같은 파일 구분해줌.
		File file = new File(pathName);
		mImg.transferTo(file); // 저장

		// String mName, int mPrice, boolean mInven, String mImg, Category cId
		// DB 저장
		Category newcId = adminService.findCategoryId(cId);
		System.out.println("image" + mImg);
		Menu inputMenu = new Menu(mName, mPrice, mInven, pathName, fileName, newcId);

		Menu findMenu = adminService.findMenuName(mName);
		if (findMenu == null) {
			adminService.addMenu(inputMenu); // 없는 경우 추가 있는 경우 변경 -> save함수
			return "<h2>상품 등록이 완료되었습니다. </h2>" + "<meta http-equiv=\"refresh\" content=\"2;url=/addmenu\" />";
		} else {
			return "<h2>상품 등록 실패. </h2>" + "<meta http-equiv=\"refresh\" content=\"2;url=/addmenu\" />";
		}
	}

	@DeleteMapping("/deleteMenu")
	public Result deleteMenu(@RequestBody Menu menu) {
		System.out.println("asdf");
		Menu deleteMenu = adminService.findMenuId(menu.getmId());
		if (deleteMenu == null) {
			return new Result("no"); // 삭제 실패
		} else {
			adminService.deleteMenu(menu.getmId()); // 없는 경우 추가 있는 경우 변경 -> save함수
			return new Result("ok");
		}
	}

	@PutMapping("/updatePrice") // 메뉴 가격 수정
	public Result updateMenu(@RequestBody MenuDTO menu) throws Exception {
		Menu updatePrice = adminService.findMenuId(menu.getmId());
		System.out.println("1 " + updatePrice.getmId());
		System.out.println("2 " + updatePrice.getmPrice());
		System.out.println("3 " + menu.getNewmPrice());

			updatePrice.setmPrice(menu.getNewmPrice());

			adminService.save(updatePrice);
//            updatePrice.setmPrice(menu.getNewmPrice());
			System.out.println("4 " + menu.getNewmPrice());

			return new Result("ok"); // 가격 수정 성공
	}

	// 메뉴 재고 수정 : updateInven
	@PutMapping("/updateInven") // 메뉴 재고 수정
	public Menu updateInven(@RequestBody MenuDTO menu) throws Exception {
		System.out.println("menu = " + menu.getmId());
		Menu updateInven = adminService.findMenuId(menu.getmId());
		updateInven.setmInven(!updateInven.ismInven());
		System.out.println("updateInven.ismInven() = " + updateInven.ismInven());
		adminService.save(updateInven);
//            updatePrice.setmPrice(menu.getNewmPrice());

		return updateInven;
	}

	// 카테고리 수정 : updateCategory
	@PostMapping("/updateCategory") // 메뉴 재고 수정
	public String updateCategory(MenuDTO menu) throws Exception {
		System.out.println("menu = " + menu.getmId());
		Menu updateCategory = adminService.findBymId(menu.getmId());
		System.out.println("menu.getcId().getcId() = " + menu.getcId().getcId());
		updateCategory.setcId(menu.getcId());

		adminService.save(updateCategory);
//            updatePrice.setmPrice(menu.getNewmPrice());

		return "<h2>카테고리 변경이 완료되었습니다. <br>" + "3초 뒤에 메뉴 목록으로 이동합니다.</h2>"
				+ "<meta http-equiv=\"refresh\" content=\"2;url=/addmenu\" />";
	}

	// 메뉴명 수정
	@PutMapping("/updatemenuname") // 메뉴명 수정
	public Result updatemenuname(@RequestBody MenuDTO menu) throws Exception {
		System.out.println("asdf");
		Menu updatemenuname = adminService.findMenuId(menu.getmId());
		if (updatemenuname == null) { // 가격 수정 실패
			return new Result("no");
		} else {
			updatemenuname.setmName(menu.getNewmName());

			adminService.save(updatemenuname);
//            updatePrice.setmPrice(menu.getNewmPrice());
			System.out.println("4 " + menu.getNewmName());

			return new Result("ok"); // 가격 수정 성공
		}
	}

	// 이미지 수정
	// 메뉴 이미지 수정
	@PostMapping("/updateImage") // 메뉴 이미지 수정
	public String updateImage(MenuDTO menu, HttpServletRequest request) throws Exception {
		System.out.println("menu.getmId() = " + menu.getmId());
		Menu updateImage = adminService.findMenuId(menu.getmId());

		if (updateImage == null) {
			return "no";
		} else {
			String path = request.getSession().getServletContext().getRealPath("\\"); // 파일 저장할 경로 가져옴.
			String fileName = UUID.randomUUID() + menu.getmImg().getOriginalFilename();
			String pathName = path + "\\static\\images\\" + fileName; // 저장된 파일 이름과 경로 합친것
			// UUID : 랜덤값으로 이름 같은 파일 구분해줌.
			File file = new File(pathName);
			menu.getmImg().transferTo(file); // 저장
			updateImage.setmImg(pathName);
			updateImage.setmImgName(fileName);

			adminService.save(updateImage);
//            updatePrice.setmPrice(menu.getNewmPrice());
			System.out.println("4 " + menu.getNewmPrice());

			return "<h2>상품 이미지 변경이 완료되었습니다. <br>" + "3초 뒤에 메뉴 목록으로 이동합니다.</h2>"
					+ "<meta http-equiv=\"refresh\" content=\"2;url=/addmenu\" />";
		}
	}

	@PostMapping("/Pay")
	public Result Pay(@RequestBody List<OrderListDao> orderList) {
		int total = 0;
		for (int i = 0; i < orderList.size(); i++) {
			total = total + orderList.get(i).getOrlPayment();
		}

		// 저장
		Date date = new Date();
		Pay pay = new Pay(total, date);
		adminService.addPay(pay);

		for (int i = 0; i < orderList.size(); i++) {
			Menu menuId = adminService.findMenuId(orderList.get(i).getOrlId());
			OrderList orderList1 = new OrderList(menuId, pay, orderList.get(i).getorlCount(),
					orderList.get(i).getOrlPayment());
			adminService.addOrderList(orderList1);
		}

		return new Result("ok");
	}
	
	

}
