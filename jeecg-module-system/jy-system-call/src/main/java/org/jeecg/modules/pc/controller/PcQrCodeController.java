package org.jeecg.modules.pc.controller;

import com.google.zxing.WriterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.QRCodeGeneratorUtil;
import org.jeecg.common.util.QRCodeUtil;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.service.ISysTenantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/12/25 10:38
 **/
@Api(tags="医院-二维码生成")
@RestController
@RequestMapping("/pc/qrCode")
@Slf4j
public class PcQrCodeController {
    @Resource
    private ISysTenantService sysTenantService;

    @ApiOperation(value="生成医院二维码", notes="生成医院二维码")
    @GetMapping(value = "/qRCodeBase64")
    public Result qRCodeBase64(String targetUrl, HttpServletResponse response) throws IOException, WriterException {
        SysTenant sysTenant = sysTenantService.getById(1016);
        String base64 = QRCodeGeneratorUtil.creatQrImage("test", sysTenant.getCompanyLogo(),"就医请扫码");
        return Result.OK(base64);
    }

    @ApiOperation(value="生成医院二维码（带log、底部文字）", notes="生成医院二维码（带log、底部文字）")
    @GetMapping("/getQrCode")
    public Result getQrCode1(HttpServletResponse response) throws IOException {
        ServletOutputStream os = response.getOutputStream();
        SysTenant sysTenant = sysTenantService.getById(1016);
        String base64 = QRCodeUtil.getQRCodeImage("test", "底部文字", "顶部文字",sysTenant.getCompanyLogo());
        return Result.OK(base64);
    }
}
