package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.config.ApplicationConfig;
import kr.makeappsgreat.onlinemall.config.SecurityConfig;
import kr.makeappsgreat.onlinemall.model.Address;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

class TestMember {

    private static PasswordEncoder passwordEncoder = new SecurityConfig().passwordEncoder();
    private static ModelMapper modelMapper = new ApplicationConfig().modelMapper();

    public static Member getWithNotAdaptToAccount() { return get("김가연", "user@domain.com", false); }
    public static Member get() { return get("김가연", "user@domain.com", true); }
    public static Member get(String name, String username) { return get(name, username, true); }
    public static Member get(String name, String username, boolean adaptToAccount) {
        AgreementRequest agreementRequest = new AgreementRequest();
        MarketingRequest marketingRequest = new MarketingRequest();
        marketingRequest.setAcceptance(false);

        agreementRequest.setTerms1(true);
        agreementRequest.setTerms2(true);
        agreementRequest.setTerms3(true);
        agreementRequest.setMarketing(marketingRequest);
        Agreement agreement = modelMapper.map(agreementRequest, Agreement.class);

        Address address = new Address();
        address.setZipcode("42700");
        address.setAddress("대구광역시 달서구");

        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setName(name);
        memberRequest.setEmail(username);
        memberRequest.setPassword("simple");
        memberRequest.setAddress(address);
        memberRequest.setMobileNumber("010-1234-5678");

        Member member = modelMapper.map(memberRequest, Member.class);
        member.setAgreement(agreement);
        if (adaptToAccount) member.adaptToAccount(passwordEncoder);


        return member;
    }
}