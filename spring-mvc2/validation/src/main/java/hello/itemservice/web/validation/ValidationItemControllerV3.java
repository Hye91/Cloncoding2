package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

    //@PostMapping("/add")
    public String addItem(@Validated/*Item에 대해서 검증기가 적용된다*/ @ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, Model model) {
        //@Validated를 사용하면 beanValidation이 다른 검증기를 추가하지 않아도 바로 적용이 되게 된다.

        //특정 필드가 아닌 복합 룰 검증 (globalError)
        //objectError은 값이 남아있는게 없기때문에 rejectedValue나 bindingFailure 추가하지않는다
        if(item.getQuantity() != null && item.getPrice() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
                //reject는 Object , Object 이름은 bindingResult가 이미 알고 있기때문에 여기서 끝
            }
        }

        //검증에 실패하면 다시 입력 폼으로! 오류값또한 같이 입력폼에 보이도록 보내줘야한다.
        if(bindingResult.hasErrors()/*!errors.isEmpty()*/){
            log.info("errors ={} " , bindingResult);
            return "validation/v3/addForm"; //검증 실패시 그냥 입력뷰로 보내버리는것
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItem2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes, Model model) {
        //@Validated를 사용하면 beanValidation이 다른 검증기를 추가하지 않아도 바로 적용이 되게 된다.
        //@Validated(SaveCheck.class)이렇게 되면 SaveCheck.class인 조건만 사용되게 된다

        //특정 필드가 아닌 복합 룰 검증 (globalError)
        //objectError은 값이 남아있는게 없기때문에 rejectedValue나 bindingFailure 추가하지않는다
        if(item.getQuantity() != null && item.getPrice() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
                //reject는 Object , Object 이름은 bindingResult가 이미 알고 있기때문에 여기서 끝
            }
        }

        //검증에 실패하면 다시 입력 폼으로! 오류값또한 같이 입력폼에 보이도록 보내줘야한다.
        if(bindingResult.hasErrors()/*!errors.isEmpty()*/){
            log.info("errors ={} " , bindingResult);
            return "validation/v3/addForm"; //검증 실패시 그냥 입력뷰로 보내버리는것
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

    //@PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

        //특정 필드가 아닌 복합 룰 검증 (globalError)
        //objectError은 값이 남아있는게 없기때문에 rejectedValue나 bindingFailure 추가하지않는다
        if(item.getQuantity() != null && item.getPrice() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
                //reject는 Object , Object 이름은 bindingResult가 이미 알고 있기때문에 여기서 끝
            }
        }

        //오류가 있는 경우 폼으로 돌아가기
        if(bindingResult.hasErrors()){
            log.info( "errors = {}" + bindingResult);
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {

        //특정 필드가 아닌 복합 룰 검증 (globalError)
        //objectError은 값이 남아있는게 없기때문에 rejectedValue나 bindingFailure 추가하지않는다
        if(item.getQuantity() != null && item.getPrice() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
                //reject는 Object , Object 이름은 bindingResult가 이미 알고 있기때문에 여기서 끝
            }
        }

        //오류가 있는 경우 폼으로 돌아가기
        if(bindingResult.hasErrors()){
            log.info( "errors = {}" + bindingResult);
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}

